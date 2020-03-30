const pkg = require("../package.json");
const child_process = require("child_process");
const path = require("path");
const os = require("os");
const killTree = require("tree-kill");

(function() {
    const gradlewCwd = path.join(__dirname, "..");
    const gradlew = path.join(gradlewCwd, "gradlew");
    const avdName = pkg.name;
    const avdImage = `system-images;android-27;default;x86`;
    const cmdPostfix = os.platform() === "win32" ? ".bat" : "";

    class ConnectedCheck {
        constructor() {}
        /**
         * Install avd image
         */
        async installImage() {
            return await this.spawn(`sdkmanager${cmdPostfix}`, [
                "--install",
                avdImage
            ]);
        }
        /**
         * Create android virtual device
         */
        async createAvd() {
            return await this.spawn(`avdmanager${cmdPostfix}`, [
                "create",
                "avd",
                "--force",
                "--name",
                avdName,
                "-k",
                avdImage,
                "--device",
                "Nexus 5X",
                "--sdcard",
                "512M"
            ]);
        }
        /**
         * Delete avd
         */
        async deleteAvd() {
            return await this.spawn(`avdmanager${cmdPostfix}`, [
                "delete",
                "avd",
                "-n",
                avdName
            ]);
        }
        /**
         * List avd
         */
        async listAvd() {
            return await this.spawn(`avdmanager${cmdPostfix}`, ["list", "avd"]);
        }
        /**
         * Run gradle task connectedCheck
         */
        async connectedCheck() {
            return await this.spawn(
                `${gradlew}${cmdPostfix}`,
                ["connectedCheck"],
                { cwd: gradlewCwd }
            );
        }
        /**
         * Starts emulator detached so that connected check can run on emulator in
         * parallel.
         * Waits till emulator has boot completed
         *
         * @returns {Promise<any>} Promise that resolves when emulator has booted
         */
        async startEmulator() {
            return new Promise((resolve, reject) => {
                const cmd = "emulator";
                console.log("spawn", cmd);

                this.emulatorProcess = child_process.spawn(
                    `${cmd}`,
                    [`@${avdName}`, "-no-audio", "-no-window"],
                    {
                        detached: true,
                        windowsHide: true
                    }
                );

                this.emulatorProcess.stdout.on("data", data => {
                    let dataString = `${data}`;
                    console.log(dataString);

                    // indicator that emulator has boot completed
                    if (dataString.includes("boot completed")) {
                        resolve();
                    }
                });

                this.emulatorProcess.stderr.on("data", data => {
                    console.error(`${data}`);
                });

                this.emulatorProcess.on("close", code => {
                    console.log(`(${cmd}) exited with code ${code}`);
                    reject("should have booted");
                });
            });
        }
        /**
         * Stop emulator
         */
        async stopEmulator() {
            return new Promise((resolve, reject) => {
                this.emulatorProcess.on("close", code => {
                    resolve(code);
                });

                this.emulatorProcess.on("exit", code => {
                    resolve(code);
                });

                //process.kill(-this.emulatorProcess.pid);
                //this.emulatorProcess.kill();
                killTree(this.emulatorProcess.pid, reason => {
                    if (reason) {
                        reject(reason);
                    } else {
                        resolve();
                    }
                });
            });
        }
        /**
         * List devices
         */
        async adbDevices() {
            return await this.spawn(`adb`, ["devices"]);
        }
        /**
         * Spawns a new process for given command and resolves when child process closed.
         *
         * @param {string} cmd
         * @param {ReadonlyArray<string>} args
         * @param {SpawnOptions} options
         */
        async spawn(cmd, args, options) {
            return new Promise((resolve, reject) => {
                console.log("spawn", cmd);

                const cp = child_process.spawn(cmd, args, options);

                cp.stdout.on("data", data => {
                    console.log(`${data}`);
                });

                cp.stderr.on("data", data => {
                    console.error(`${data}`);
                });

                cp.on("close", code => {
                    console.log(`${cmd} exited with code ${code}`);
                    if (code) {
                        reject(code);
                    } else {
                        resolve(code);
                    }
                });
            });
        }

        /**
         * Run all steps for connected check
         */
        async run() {
            await this.installImage();
            await this.createAvd();
            await this.listAvd();
            await this.startEmulator();
            await this.adbDevices();

            try {
                await this.connectedCheck();
            }
            catch (e) {
                console.error(e);
            }

            await this.stopEmulator();
            await this.adbDevices();
            await this.deleteAvd();
            await this.listAvd();

            console.info("success");
        }
    }

    return new ConnectedCheck()
        .run()
        .catch(console.error);
})();

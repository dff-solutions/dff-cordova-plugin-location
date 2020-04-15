# Usage Realm

This plugin is available via the global `LocationPlugin.realm`.

This part of the plugin provides common database functionality.

<!-- TOC depthFrom:2 -->

- [Interfaces](#interfaces)
    - [DatabaseInformation](#databaseinformation)
    - [RealmConfiguration](#realmconfiguration)
- [Actions](#actions)
    - [compact](#compact)
    - [delete](#delete)
    - [deleteAll](#deleteall)
    - [getDatabaseInformation](#getdatabaseinformation)

<!-- /TOC -->

## Interfaces

### DatabaseInformation

```typescript
interface DatabaseInformation {
    size: number;
    globalInstanceCount: number;
    localInstanceCount: number;
    locationCount: number;
    proximityAlertCount: number;
    realmConfiguration: RealmConfiguration;
}
```

### RealmConfiguration

See [RealmConfiguration] for more details.

## Actions

### compact

```ts
/**
 * Compacts realm database.
 *
 * @return {number} 1 if success, 0 otherwise
 */
LocationPlugin
    .realm
    .compact(
        (success) => console.info(!!success),
        console.error
    );
```

### delete

```ts
/**
 * Deletes Realm files along with related temporary files.
 *
 * @return {number} 1 if success, 0 otherwise
 */
LocationPlugin
    .realm
    .delete();
```

### deleteAll

```ts
/**
 * Delete all objects from realm.
 */
LocationPlugin
    .realm
    .deleteAll();
```

### getDatabaseInformation

```ts
/**
 * Get realm database information.
 *
 * @return {DatabaseInformation} information from database
 */
LocationPlugin
    .realm
    .getDatabaseInformation(
        console.info,
        console.error
    );
```

[RealmConfiguration]: ./USAGE_REALM.md#RealmConfiguration

package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.core.helpers.LocationDiagnosticsHelper;

import org.apache.cordova.CallbackContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static junit.framework.TestCase.assertEquals;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReadDiagnosticsUnitTest {

    @Mock
    private LocationDiagnosticsHelper locationDiagnosticsHelper;

    @Mock
    private CallbackContext callbackContext;

    @InjectMocks
    private ReadDiagnostics readDiagnostics;

    @BeforeEach
    void setup() {
        readDiagnostics.setCallbackContext(callbackContext);
    }

    @Test
    void shouldReturnCorrectActionName() {
        assertEquals("readDiagnostics", readDiagnostics.getActionName());
    }

    @Test
    void shouldReturnJson() throws JSONException {
        JSONObject json = mock(JSONObject.class);
        when(locationDiagnosticsHelper.getDiagnosticsJson()).thenReturn(json);

        readDiagnostics.execute();

        verify(callbackContext).success(json);
    }
}

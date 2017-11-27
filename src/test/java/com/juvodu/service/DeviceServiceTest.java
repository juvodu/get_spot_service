package com.juvodu.service;

import com.juvodu.service.testmodel.DeviceTestModel;
import com.juvodu.service.testmodel.UserTestModel;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for the DeviceService
 *
 * @author Juvodu
 */
public class DeviceServiceTest {

    // instantiate with test model to ensure persisting all data to the test table "device_test"
    private static DeviceService<DeviceTestModel> deviceService;
    private static UserService<UserTestModel> userService;

    @BeforeClass
    public static void beforeClass(){

        deviceService = new DeviceService(DeviceTestModel.class);
        userService = new UserService(UserTestModel.class);
    }

    @Before
    public void before(){

        deviceService.deleteAll();
    }

    @Test
    public void givenUserWhenGetDevicesByUserThenReturnDevices(){

        // setup
        UserTestModel user = new UserTestModel();
        userService.save(user);
        DeviceTestModel device = new DeviceTestModel();
        device.setUserId(user.getId());
        device.setDeviceToken("123");
        deviceService.save(device);

        // execute
        List<DeviceTestModel> devices = deviceService.getDevicesByUser(user.getId(), 100);

        // verify
        assertNotNull(devices);
        assertEquals(1, devices.size());
        assertEquals(user.getId(), devices.get(0).getUserId());
    }
}

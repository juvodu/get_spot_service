package com.juvodu.database.model;

/**
 * Different platforms for user devices
 *
 * @author Juvodu
 */
public enum Platform {
    // Apple Push Notification Service
    APNS,
    // Sandbox version of Apple Push Notification Service
    APNS_SANDBOX,
    // Amazon Device Messaging
    ADM,
    // Google Cloud Messaging
    GCM,
    // Baidu CloudMessaging Service
    BAIDU,
    // Windows Notification Service
    WNS,
    // Microsoft Push Notificaion Service
    MPNS;
}

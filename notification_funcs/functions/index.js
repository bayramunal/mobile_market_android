"use strict";

const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database
  .ref("/notifications/{user_id}/{notification_id}")
  .onWrite((change, context) => {
    const user_id = context.params.user_id;
    const notification = context.params.notification;

    console.log("we have a notification to send to : ", user_id);

    if (!change.after.val()) {
      return console.log(
        "a notification has been deleted from the database : ",
        notification
      );
    }

    const from_user = admin
      .database()
      .ref(`/notifications/${user_id}/${notification}`)
      .once("value");

    return from_user.then(fromUserResult => {
      const from_user_id = fromUserResult.val();

      console.log("you have new notification from uid : ", from_user_id);

      const deviceToken = admin
        .database()
        .ref(`/_users/${user_id}/device_token`)
        .once("value");

      return deviceToken.then(result => {
        const token_id = result.val();

        const payload = {
          notification: {
            title: "Friend Request",
            body: "You have a received a new friend request!",
            icon: "default"
          }
        };

        return admin
          .messaging()
          .sendToDevice(token_id, payload)
          .then(response => {
            return console.log("This was the notification Feature");
          });
      });
    });
  });

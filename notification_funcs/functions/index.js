'use strict'

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.sendNotification = functions.database.ref('/notifications/{user_id}/notification_id').onWrite((change, context) => {

	const user_id = context.params.user_id;
	const notification = context.params.notification;

	console.log('we have a notification to sen to : ', user_id);
	
	if (!context.data.val()) {
		return console.log('a notitification has been deleted from the database : ' + notification_id);
	}
	
	const deviceToken = admin.database().ref(`/_users/${user_id}/device_token`).once('value');
	
	return deviceToken.then(result => {
		
		const token_id = result.val();
		
		const payload = {
			notification : {
				title : "friend request",
				body : "you have received a new friend request",
				icon : "default
			}
		};
	
		return admin.messaging().sendToDevice(token_id ,payload).then(response => {
			
			console.log('this was the notification feature');
			
		});
		
	});
	
	
});
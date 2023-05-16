/********* jedlixplugin.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>

@interface jedlixplugin : CDVPlugin {
  // Member variables go here.
}

- (void)coolMethod:(CDVInvokedUrlCommand*)command;
@end

@implementation jedlixplugin

- (void)coolMethod:(CDVInvokedUrlCommand*)command
{
	// F. Grooten: this line below is the ONLY line added by Tomas. The rest is default boiler plate cordova plugin
    //[[NSNotificationCenter defaultCenter] postNotificationName:@"connect" object:self];
	[[[UIApplication shared] keyWindow].rootViewController presentViewController:UIViewController() animated:YES completion:NULL];
	
    CDVPluginResult* pluginResult = nil;
    NSString* echo = [command.arguments objectAtIndex:0];

    if (echo != nil && [echo length] > 0) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:echo];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end

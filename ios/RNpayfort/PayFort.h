#import <React/RCTBridgeModule.h>
#import <React/RCTEventDispatcher.h>
#import <React/RCTUtils.h>
#import <React/RCTLog.h>
#include <CommonCrypto/CommonDigest.h>
#import <UIKit/UIKit.h>
#import <PayFortSDK/PayFortSDK.h>
@interface PayFort : NSObject <RCTBridgeModule>
@property (nonatomic) PayFortController *payFort;

@end

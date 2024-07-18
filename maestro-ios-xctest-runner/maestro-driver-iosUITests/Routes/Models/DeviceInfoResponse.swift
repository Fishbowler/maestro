import Foundation

struct DeviceInfoResponse: Codable {
    let platformVersion: String
    let widthPoints: Int
    let heightPoints: Int
    let widthPixels: Int
    let heightPixels: Int
}

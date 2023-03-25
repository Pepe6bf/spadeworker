package site.devtown.spadeworker.global.util;

public class ImageUtil {

    public static String getImageExtension(String imageFileName) {
        return imageFileName.substring(imageFileName.lastIndexOf(".") + 1);
    }

    public static String getStoredImageName(String localStorageImageUri) {
        return localStorageImageUri.substring(localStorageImageUri.lastIndexOf("/") + 1);
    }

    public static String getStoredImageResourcePath(String storedImageFullPath) {
        return storedImageFullPath.substring(storedImageFullPath.lastIndexOf(".com/") + 5);
    }
}
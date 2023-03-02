package site.devtown.spadeworker.global.util;

public class ImageUtil {

    public static String getImageExtension(String imageFileName) {
        return imageFileName.substring(imageFileName.lastIndexOf(".") + 1);
    }

    public static String getLocalStorageImageName(String localStorageImageUri) {
        return localStorageImageUri.substring(localStorageImageUri.lastIndexOf("/") + 1);
    }
}

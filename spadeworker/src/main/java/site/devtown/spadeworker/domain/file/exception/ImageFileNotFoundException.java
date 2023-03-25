package site.devtown.spadeworker.domain.file.exception;

import static site.devtown.spadeworker.domain.file.exception.FileExceptionCode.IMAGE_FILE_NOT_FOUND;

public class ImageFileNotFoundException
        extends RuntimeException {

    public ImageFileNotFoundException() {
        super(IMAGE_FILE_NOT_FOUND.getMessage());
    }
}

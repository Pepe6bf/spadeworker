package site.devtown.spadeworker.domain.file.exception;

public class ImageFileNotFoundException
        extends RuntimeException {

    public ImageFileNotFoundException() {
        super("이미지 리소스가 존재하지 않습니다.");
    }
}

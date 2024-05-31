package mate.apple_tree_reservation.exception;

public class ResourceNotFoundException extends RuntimeException {
    private String detailMessage;

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, String detailMessage) {
        super(message);
        this.detailMessage = detailMessage;
    }

    public String getDetailMessage() {
        return detailMessage;
    }
}
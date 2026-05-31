package studydoc.port;

public record KeycloakUserRepresentation(
        String id,
        String username,
        String email,
        String firstName,
        String lastName
) {
    public String fullName() {
        if (firstName == null && lastName == null) {
            return null;
        }
        if (firstName == null) {
            return lastName;
        }
        if (lastName == null) {
            return firstName;
        }
        return firstName + " " + lastName;
    }
}

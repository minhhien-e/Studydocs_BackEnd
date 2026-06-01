package studydocs.media.service;

public interface IdempotencyService {
    String get(String key);
    void save(String key,String value, long expireInSeconds);
}

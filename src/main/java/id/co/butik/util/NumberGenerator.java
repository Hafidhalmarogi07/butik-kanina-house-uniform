package id.co.butik.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Service
public class NumberGenerator {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public String generateInvoiceNumber() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String redisKey = "invoice:sequence:" + date;

        // INCR akan otomatis menambah dan buat key jika belum ada
        Long sequence = redisTemplate.opsForValue().increment(redisKey);

        // Optional: atur expired key selama 2 hari agar Redis tidak penuh
        if (sequence == 1L) {
            redisTemplate.expire(redisKey, 2, TimeUnit.DAYS);
        }

        String formattedSequence = String.format("%04d", sequence);
        return "INV-" + date + "-" + formattedSequence;
    }

    public String generateOrderNumber() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String redisKey = "order:sequence:" + date;

        // INCR akan otomatis menambah dan buat key jika belum ada
        Long sequence = redisTemplate.opsForValue().increment(redisKey);

        // Optional: atur expired key selama 2 hari agar Redis tidak penuh
        if (sequence == 1L) {
            redisTemplate.expire(redisKey, 2, TimeUnit.DAYS);
        }

        String formattedSequence = String.format("%04d", sequence);
        return "ORD-" + date + "-" + formattedSequence;
    }


}

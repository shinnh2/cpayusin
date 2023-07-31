package com.jbaacount.visitor.service;

import com.jbaacount.visitor.dto.VisitorResponseDto;
import com.jbaacount.visitor.entity.Visitor;
import com.jbaacount.visitor.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class VisitorService
{
    private final RedisTemplate<String, String> redisTemplate;
    private final VisitorRepository visitorRepository;

    @Scheduled(initialDelay = 180000, fixedDelay = 180000)
    public void updateVisitorData()
    {
        Set<String> keys = redisTemplate.keys("*_*");

        for (String key : keys)
        {
            String[] parts = key.split("_");
            String ipAddress = parts[0];
            LocalDate date = LocalDate.parse(parts[1]);

            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            String userAgent = valueOperations.get(key);

            if(!visitorRepository.existsByIpAddressAndDate(ipAddress, date))
            {
                Visitor visitor = Visitor.builder()
                        .ipAddress(ipAddress)
                        .userAgent(userAgent)
                        .date(date)
                        .build();

                log.info("===visitor scheduler===");
                log.info("visitor info saved = {}", ipAddress);
                visitorRepository.save(visitor);
            }

            log.info("visitor info terminated in redis");
            redisTemplate.delete(key);
        }
    }

    public VisitorResponseDto getVisitorResponse()
    {
        LocalDate today = LocalDate.now();

        VisitorResponseDto response = VisitorResponseDto.builder()
                .yesterday(getYesterdayVisitors(today))
                .today(getTodayVisitors(today))
                .total(getTotalVisitors())
                .build();

        return response;
    }

    private Long getTodayVisitors(LocalDate date)
    {
        Long count = visitorRepository.countByDate(date);

        return count != null ? count : 0;
    }

    private Long getYesterdayVisitors(LocalDate date)
    {
        Long count = visitorRepository.countByDate(date.minusDays(1));

        return count != null ? count : 0;
    }

    private Long getTotalVisitors()
    {
        Long count = visitorRepository.count();

        return count != null ? count : 0;
    }
}

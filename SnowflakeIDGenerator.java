package cn.test;

import java.util.ArrayList;
import java.util.Collections;

public class SnowflakeIDGenerator{
	public static void main(String[] args) {
		ArrayList<Long> arrayList = new ArrayList<>();

		long generateID =0L;
		for (long i = 0L; i < 10; i++) {
			SnowflakeIDGeneratorUtils generatorUtils = new SnowflakeIDGeneratorUtils(i,i);
			generateID = generatorUtils.generateID();
			System.out.println( "index = "+i+" ; "+generateID);
			arrayList.add(generateID);
		}

		Collections.sort(arrayList);
		for (Long item : arrayList) {
			System.out.println(item);
		}
	}
}


class SnowflakeIDGeneratorUtils {
	private long dataCenterId;
	private long workerId;
	private long sequence = 0L;
	private long lastTimestamp = -1L;

	// 构造函数初始化数据中心ID和工作机器ID
	public SnowflakeIDGeneratorUtils(long dataCenterId, long workerId) {
		this.dataCenterId = dataCenterId;
		this.workerId = workerId;
	}

	// 生成雪花ID
	public synchronized long generateID() {
		long timestamp = System.currentTimeMillis();

		if (timestamp < lastTimestamp) {
			throw new RuntimeException("Invalid timestamp!");
		}

		if (timestamp == lastTimestamp) {
			sequence = (sequence + 1) & 4095; // 4095是序列号的最大值
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0L;
		}

		lastTimestamp = timestamp;

		return ((timestamp - 1609459200000L) << 22) | (dataCenterId << 17) | (workerId << 12) | sequence;
	}

	// 当前时间戳小于等于上次生成ID的时间戳，需要等待下一毫秒
	private long tilNextMillis(long lastTimestamp) {
		long timestamp = System.currentTimeMillis();
		while (timestamp <= lastTimestamp) {
			timestamp = System.currentTimeMillis();
		}
		return timestamp;
	}
}

package com.nht.aop_demo.business;

import com.nht.aop_demo.data.DataService1;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class BusinessService1 {

    private final DataService1 dataService1;

    public BusinessService1(DataService1 dataService1) {
        this.dataService1 = dataService1;
    }

    public int calculateMax() {
        int[] data = dataService1.retrieveData();
        return Arrays.stream(data).max().orElse(0);
    }

    public int power(int base, int power) {
        return (int) Math.pow(base, power);
    }

    public long findResourceById(long id) {
        return dataService1.findById(id);
    }

    public long findResourceByIdSuccess(long id) {
        return dataService1.findByIdSuccess(id);
    }
}

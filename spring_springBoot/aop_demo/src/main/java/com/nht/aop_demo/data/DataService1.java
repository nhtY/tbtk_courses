package com.nht.aop_demo.data;

import org.springframework.stereotype.Repository;

@Repository
public class DataService1 {

    public int[] retrieveData() {
        return new int[] {11, 22, 33, 44, 55};
    }

    public long findById(long id) {
        throw new RuntimeException("Could not find the resource with id: " + id);
    }

    public long findByIdSuccess(long id) {
        return id;
    }
}

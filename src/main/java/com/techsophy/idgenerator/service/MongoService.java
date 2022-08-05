package com.techsophy.idgenerator.service;

public interface MongoService
{
    Object getNextSequence(String id, String column);
}

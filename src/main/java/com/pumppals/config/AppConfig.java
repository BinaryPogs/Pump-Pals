package com.pumppals.config;

import com.google.inject.AbstractModule;
import com.pumppals.dao.UserDao;
import com.pumppals.dao.ChallengeDao;
import com.pumppals.dao.interfaces.IUserDao;

public class AppConfig extends AbstractModule {
    @Override
    protected void configure() {
        bind(UserDao.class).asEagerSingleton();
        bind(ChallengeDao.class).asEagerSingleton();
        bind(IUserDao.class).to(UserDao.class);
    }
}
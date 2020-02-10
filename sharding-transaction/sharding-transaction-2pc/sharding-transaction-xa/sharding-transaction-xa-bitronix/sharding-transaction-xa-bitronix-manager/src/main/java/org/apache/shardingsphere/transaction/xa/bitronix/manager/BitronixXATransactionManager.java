/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.transaction.xa.bitronix.manager;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.ResourceRegistrar;
import lombok.SneakyThrows;
import org.apache.shardingsphere.transaction.xa.spi.SingleXAResource;
import org.apache.shardingsphere.transaction.xa.spi.XATransactionManager;

import javax.sql.XADataSource;
import javax.transaction.TransactionManager;

/**
 * Bitronix sharding transaction manager.
 *
 * @author zhaojun
 */
public final class BitronixXATransactionManager implements XATransactionManager {
    
    private final BitronixTransactionManager bitronixTransactionManager = TransactionManagerServices.getTransactionManager();
    
    @Override
    public void init() {
    }
    
    @SneakyThrows
    @Override
    public void registerRecoveryResource(final String dataSourceName, final XADataSource xaDataSource) {
        ResourceRegistrar.register(new BitronixRecoveryResource(dataSourceName, xaDataSource));
    }
    
    @SneakyThrows
    @Override
    public void removeRecoveryResource(final String dataSourceName, final XADataSource xaDataSource) {
        ResourceRegistrar.unregister(new BitronixRecoveryResource(dataSourceName, xaDataSource));
    }
    
    @SneakyThrows
    @Override
    public void enlistResource(final SingleXAResource singleXAResource) {
        bitronixTransactionManager.getTransaction().enlistResource(singleXAResource);
    }
    
    @Override
    public TransactionManager getTransactionManager() {
        return bitronixTransactionManager;
    }
    
    @Override
    public void close() {
        bitronixTransactionManager.shutdown();
    }
}

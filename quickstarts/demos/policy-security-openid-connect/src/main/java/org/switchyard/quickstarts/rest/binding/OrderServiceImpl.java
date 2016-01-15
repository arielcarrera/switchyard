/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.switchyard.quickstarts.rest.binding;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.switchyard.component.bean.Service;

/**
 * An OrderService implementation.
 *
 * @author Ariel Carrera <carreraariel@gmail.com>
 */
@Service(OrderService.class)
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = Logger.getLogger(OrderService.class);


    public Order getOrder(Integer orderId) throws Exception {
        LOGGER.info("Getting Order with no: " + orderId);

        Order order = new Order(orderId);
        Item item = new Item();
        item.setItemId(orderId * 1000);
        item.setName("Item");
        OrderItem orderItem = new OrderItem(item);
        List<OrderItem> items = new ArrayList<OrderItem>();
        items.add(orderItem);
        order.setItems(items);

        return order;
    }

}

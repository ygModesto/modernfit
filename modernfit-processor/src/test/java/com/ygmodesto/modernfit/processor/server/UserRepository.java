/*
 * Copyright 2020 Yago Modesto González Diéguez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ygmodesto.modernfit.processor.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {

  private static Map<Long, User> users;

  static {
    users = new HashMap<Long, User>();

    User user = new User(1L, "Juan", "@juan");
    users.put(user.getId(), user);
    user = new User(2L, "Luis", "@luis");
    users.put(user.getId(), user);
    user = new User(3L, "Ana", "@ana");
    users.put(user.getId(), user);
    user = new User(4L, "Miriam", "@miriam");
    users.put(user.getId(), user);
  }

  public User findById(Long userId) {
    return users.get(userId);
  }

  public Collection<User> findByIds(Collection<Long> usersId) {

    Collection<User> usersAux = new ArrayList<User>();

    for (Long userId : usersId) {
      usersAux.add(users.get(userId));
    }

    return usersAux;
  }
}

package com.example.common.log;


import com.example.models.domain.Behavior;

import java.util.List;

public interface IBehaviorService {


   int delete(String username);

   List<Behavior> list(String username);

   void saveBehavior(String username, String action);

}
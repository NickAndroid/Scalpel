/*
 * Copyright (c) 2016 Nick Guo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nick.scalpeldemo;

import com.nick.scalpel.Scalpel;
import com.nick.scalpel.ScalpelApplication;
import com.nick.scalpel.annotation.opt.ContextConfiguration;

@ContextConfiguration(xmlRes = R.xml.context_scalpel_demo)
public class MyApplication extends ScalpelApplication {
    @Override
    protected void onConfigScalpel(Scalpel scalpel) {
        super.onConfigScalpel(scalpel);
        scalpel.addFieldWirer(new CustomWirer());
    }
}
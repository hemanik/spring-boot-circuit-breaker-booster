/*
 * Copyright 2016-2017 Red Hat, Inc, and individual contributors.
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

package io.openshift.booster.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Name service controller.
 */
@RestController
public class FallbackController {

    private static final Logger LOG = LoggerFactory.getLogger(FallbackController.class);

    public static final String theName = "Fallback World";

    @RequestMapping("/api/ping")
    public StateInfo getPing() throws Exception {
        return StateInfo.OK;
    }

    /**
     * Endpoint to get a name.
     *
     * @return Host name.
     */
    @RequestMapping("/api/name")
    public ResponseEntity<String> getName() throws IOException {
        LOG.info(String.format("Returning a name '%s'", theName));
        return new ResponseEntity<>(theName, HttpStatus.OK);
    }

    /**
     * Set name service state.
     */
    @RequestMapping("/api/info")
    public StateInfo getState() throws Exception {
        return StateInfo.OK;
    }

    static class StateInfo {
        private String state;

        static final StateInfo OK = new StateInfo("ok");
        static final StateInfo FAIL = new StateInfo("fail");

        public StateInfo() {
        }

        public StateInfo(String state) {
            this.state = state;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
}

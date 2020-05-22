/*
 *     Copyright 2020 Universitas Teknologi Yogyakarta @ https://uty.ac.id
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tokobuku;

import tokobuku.util.PreferencedHelper;
import tokobuku.view.DashboardPegawaiView;
import tokobuku.view.LoginView;

/**
 *
 * @author Muhammad Rosyid Izzulkhaq (5180411122)
 * @author Taufik Ismail (5180411074)
 * @author Didik Nowo Hariyanto Widodo (5180411081)
 * @author Risma Nur Oktaviani B. (5180411006)
 * @author Atika Inayatu Alfiyah (5180411028)
 * @version 1.0
 */
public class SistemTokoBuku {

    final static PreferencedHelper PREFS = new PreferencedHelper();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (PREFS.getLogin()) {
            DashboardPegawaiView dashboard = new DashboardPegawaiView();
            dashboard.setVisible(true);
        } else {
            LoginView login = new LoginView();
            login.setVisible(true);
        }
    }
}

package com.example.fujinohiroki.homey.models;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.FieldAttribute;
import io.realm.Realm;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;
import io.realm.internal.ColumnIndices;
import io.realm.internal.ColumnInfo;
import io.realm.internal.Table;

/**
 * Created by fujinohiroki on 2017/03/04.
 * Migrationクラス
 * Realmのスキーマをバージョン管理するクラス
 */

public class Migration implements RealmMigration {

    public int hashCode() {
        return Migration.class.hashCode();
    }

    public boolean equals(Object object) {
        if(object == null) {
            return false;
        }
        return object instanceof Migration;
    }

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        RealmSchema schema = realm.getSchema();
        /*
            UserモデルのidをPrimaryKeyに設定する
            @Primary
            long id;
            String name;
            String password;
            String email;
         */
        if(oldVersion == 0) {
            schema.get("User")
                    .addField("id_tmp", long.class, FieldAttribute.PRIMARY_KEY)
                    .transform(null)
                    .removeField("id")
                    .renameField("id_tmp", "id");
            oldVersion++;
        }
    }
}

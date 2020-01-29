/*
 * Copyright (C) 2010 Jan Pokorsky
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package cz.incad.kramerius.editor.share;

import  cz.incad.kramerius.fedora.*;
import com.google.gwt.core.client.GWT;
import cz.incad.kramerius.editor.client.EditorConfiguration;
import cz.incad.kramerius.editor.client.EditorConstants;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Jan Pokorsky
 */
public final class GWTKrameriusObject {

    private static final EditorConstants I18N;

    static {
        // cannot call GWT on server dark side
        if (GWT.isClient()) {
            I18N = GWT.create(EditorConstants.class);
        } else {
            I18N = null;
        }
    }

    public enum Kind {
        MONOGRAPH,
        MONOGRAPH_UNIT,
        PERIODICAL,
        PERIODICAL_VOLUME,
        PERIODICAL_ITEM,
        PAGE,
        INTERNAL_PART,
        DONATOR,
        ARTICLE,
        MANUSCRIPT,
        MAP,
        GRAPHIC,
        SHEETMUSIC,
        SOUNDRECORDING,
        SOUNDUNIT,
        TRACK,
        ALL;

        public String toLocalizedString() {
            String localizedTxt = this.toString();
            if (I18N != null) {
                Map<String, String> i18nKinds = I18N.krameriusObjectKinds();
                localizedTxt = i18nKinds.get(this.toString());
            }
            return localizedTxt;
        }

        public String toLocalizedPluralString() {
            String localizedTxt = this.toString();
            if (I18N != null) {
                Map<String, String> i18nKinds = I18N.krameriusRelationTabNames();
                localizedTxt = i18nKinds.get("RELATION_" + this.toString());
            }
            return localizedTxt;
        }

    }

    private static final String UUID_PREFIX = "uuid:";

    private int number;
    private String pid;
    private Kind kind;
    //private String title;
    private Map<Kind, List<GWTKrameriusObject>> relationsMap;
    private Map<String,String> properties;

    /* gwt serialization purposes; DO NOT USE! */
    GWTKrameriusObject() {}

    public GWTKrameriusObject(String pid, Kind kind, int number, Map<String,String> properties) {
        this(pid, kind, number, properties, null);
    }

    public GWTKrameriusObject(String pid, Kind kind, Map<String,String> properties, Map<Kind, List<GWTKrameriusObject>> relations) {
        this(pid, kind, -1, properties, relations);
    }

    public GWTKrameriusObject(String pid, Kind kind, int number, Map<String,String> properties, Map<Kind, List<GWTKrameriusObject>> relations) {
        this.pid = pid;
        this.kind = kind;
        this.number = number;
        this.properties = properties;
        this.relationsMap = relations;
    }

    public Kind getKind() {
        return kind;
    }

    public String getPID() {
        return pid;
    }

    public int getNumber() {
        return number;
    }

    public Map<String,String> getProperties() {
        return this.properties;
    }

    public List<GWTKrameriusObject> getRelations(Kind relationKind) {
        return relationsMap.get(relationKind);
    }

    public void setRelations(Kind relationKind, List<GWTKrameriusObject> relations) {
        relationsMap.put(relationKind, relations);
    }

    public Map<Kind, List<GWTKrameriusObject>> getRelations() {
        return relationsMap;
    }

    public void setRelations(Map<Kind, List<GWTKrameriusObject>> relations) {
        this.relationsMap = relations;
    }

    public Set<Kind> getRelationKinds() {
        return relationsMap.keySet();
    }

    public String getLocation() {
        return EditorConfiguration.getInstance().getThumbnailURL(getPID());
    }

    public String getPreviewLocation() {
        return EditorConfiguration.getInstance().getPreviewURL(getPID());
    }

//    private String getUUID() {
//        String uuid = pid;
//        if (pid.startsWith(UUID_PREFIX)) {
//            uuid = pid.substring(UUID_PREFIX.length());
//        }
//        return uuid;
//    }

    @Override
    public String toString() {
        return "GWTKO[" + pid + ']'+this.properties;
    }

}
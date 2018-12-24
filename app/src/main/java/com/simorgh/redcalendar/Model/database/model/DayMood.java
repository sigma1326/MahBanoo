package com.simorgh.redcalendar.Model.database.model;

import java.util.Calendar;
import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "day_mood")
public class DayMood {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "mood_id")
    private Calendar id;

    @ColumnInfo(name = "type_bleeding")
    private int typeBleedingSelectedIndex;

    @ColumnInfo(name = "type_emotion")
    private List<Integer> typeEmotionSelectedIndices;

    @ColumnInfo(name = "type_pain")
    private List<Integer> typePainSelectedIndices;

    @ColumnInfo(name = "type_eating_desire")
    private List<Integer> typeEatingDesireSelectedIndices;

    @ColumnInfo(name = "type_hair_style")
    private List<Integer> typeHairStyleSelectedIndices;

    @ColumnInfo(name = "drugs")
    private List<String> drugs;

    @ColumnInfo(name = "weight")
    private float weight;

    public Calendar getId() {
        return id;
    }

    public void setId(Calendar id) {
        this.id = id;
    }

    public int getTypeBleedingSelectedIndex() {
        return typeBleedingSelectedIndex;
    }

    public void setTypeBleedingSelectedIndex(int typeBleedingSelectedIndex) {
        this.typeBleedingSelectedIndex = typeBleedingSelectedIndex;
    }

    public List<Integer> getTypeEmotionSelectedIndices() {
        return typeEmotionSelectedIndices;
    }

    public void setTypeEmotionSelectedIndices(List<Integer> typeEmotionSelectedIndices) {
        this.typeEmotionSelectedIndices = typeEmotionSelectedIndices;
    }

    public List<Integer> getTypePainSelectedIndices() {
        return typePainSelectedIndices;
    }

    public void setTypePainSelectedIndices(List<Integer> typePainSelectedIndices) {
        this.typePainSelectedIndices = typePainSelectedIndices;
    }

    public List<Integer> getTypeEatingDesireSelectedIndices() {
        return typeEatingDesireSelectedIndices;
    }

    public void setTypeEatingDesireSelectedIndices(List<Integer> typeEatingDesireSelectedIndices) {
        this.typeEatingDesireSelectedIndices = typeEatingDesireSelectedIndices;
    }

    public List<Integer> getTypeHairStyleSelectedIndices() {
        return typeHairStyleSelectedIndices;
    }

    public void setTypeHairStyleSelectedIndices(List<Integer> typeHairStyleSelectedIndices) {
        this.typeHairStyleSelectedIndices = typeHairStyleSelectedIndices;
    }

    public List<String> getDrugs() {
        return drugs;
    }

    public void setDrugs(List<String> drugs) {
        this.drugs = drugs;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
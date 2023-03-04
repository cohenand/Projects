package com.example.familymap.DataContainers;

public class Settings {

    public boolean LifeStoryLines;
    public boolean FamilyTreeLines;
    public boolean SpouseLines;
    public boolean FathersSide;
    public boolean MothersSide;
    public boolean MaleEvents;
    public boolean FemaleEvents;

    public Settings(boolean lifeStoryLines,boolean familyTreeLines, boolean spouseLines, boolean fathersSide, boolean mothersSide, boolean maleEvents, boolean femaleEvents) {
        this.LifeStoryLines = lifeStoryLines;
        this.FamilyTreeLines = familyTreeLines;
        this.SpouseLines = spouseLines;
        this.FathersSide = fathersSide;
        this.MothersSide = mothersSide;
        this.MaleEvents = maleEvents;
        this.FemaleEvents = femaleEvents;
    }

    public Settings() {
        this.LifeStoryLines = true;
        this.FamilyTreeLines = true;
        this.SpouseLines = true;
        this.FathersSide = true;
        this.MothersSide = true;
        this.MaleEvents = true;
        this.FemaleEvents = true;
    }

    public boolean isLifeStoryLines() {
        return LifeStoryLines;
    }

    public void setLifeStoryLines(boolean lifeStoryLines) {
        LifeStoryLines = lifeStoryLines;
    }

    public boolean isFamilyTreeLines() {
        return FamilyTreeLines;
    }

    public void setFamilyTreeLines(boolean familyTreeLines) {
        FamilyTreeLines = familyTreeLines;
    }

    public boolean isSpouseLines() {
        return SpouseLines;
    }

    public void setSpouseLines(boolean spouseLines) {
        SpouseLines = spouseLines;
    }

    public boolean isFathersSide() {
        return FathersSide;
    }

    public void setFathersSide(boolean fathersSide) {
        FathersSide = fathersSide;
    }

    public boolean isMothersSide() {
        return MothersSide;
    }

    public void setMothersSide(boolean mothersSide) {
        MothersSide = mothersSide;
    }

    public boolean isMaleEvents() {
        return MaleEvents;
    }

    public void setMaleEvents(boolean maleEvents) {
        MaleEvents = maleEvents;
    }

    public boolean isFemaleEvents() {
        return FemaleEvents;
    }

    public void setFemaleEvents(boolean femaleEvents) {
        FemaleEvents = femaleEvents;
    }
}

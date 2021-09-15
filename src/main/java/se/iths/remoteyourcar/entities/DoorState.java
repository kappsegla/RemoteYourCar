package se.iths.remoteyourcar.entities;

public class DoorState {
    private boolean df_open;
    private boolean df_locked;
    private boolean pf_open;
    private boolean pf_locked;
    private boolean dr_open;
    private boolean dr_locked;
    private boolean pr_open;
    private boolean pr_locked;
    private boolean ft_open;
    private boolean ft_locked;
    private boolean rt_open;
    private boolean rt_locked;

    public void setAllOpen() {
        df_open = pf_open = dr_open = pr_open = ft_open = rt_open = true;
    }

    public void setAllLocked() {
        df_locked = pf_locked = dr_locked = pr_locked = ft_locked = rt_locked = true;
    }

    public void setAllUnLocked() {
        df_locked = pf_locked = dr_locked = pr_locked = ft_locked = rt_locked = false;
    }

    public boolean isDf_open() {
        return df_open;
    }

    public void setDf_open(boolean df_open) {
        this.df_open = df_open;
    }

    public boolean isDf_locked() {
        return df_locked;
    }

    public void setDf_locked(boolean df_locked) {
        this.df_locked = df_locked;
    }

    public boolean isPf_open() {
        return pf_open;
    }

    public void setPf_open(boolean pf_open) {
        this.pf_open = pf_open;
    }

    public boolean isPf_locked() {
        return pf_locked;
    }

    public void setPf_locked(boolean pf_locked) {
        this.pf_locked = pf_locked;
    }

    public boolean isDr_open() {
        return dr_open;
    }

    public void setDr_open(boolean dr_open) {
        this.dr_open = dr_open;
    }

    public boolean isDr_locked() {
        return dr_locked;
    }

    public void setDr_locked(boolean dr_locked) {
        this.dr_locked = dr_locked;
    }

    public boolean isPr_open() {
        return pr_open;
    }

    public void setPr_open(boolean pr_open) {
        this.pr_open = pr_open;
    }

    public boolean isPr_locked() {
        return pr_locked;
    }

    public void setPr_locked(boolean pr_locked) {
        this.pr_locked = pr_locked;
    }

    public boolean isFt_open() {
        return ft_open;
    }

    public void setFt_open(boolean ft_open) {
        this.ft_open = ft_open;
    }

    public boolean isFt_locked() {
        return ft_locked;
    }

    public void setFt_locked(boolean ft_locked) {
        this.ft_locked = ft_locked;
    }

    public boolean isRt_open() {
        return rt_open;
    }

    public void setRt_open(boolean rt_open) {
        this.rt_open = rt_open;
    }

    public boolean isRt_locked() {
        return rt_locked;
    }

    public void setRt_locked(boolean rt_locked) {
        this.rt_locked = rt_locked;
    }
}

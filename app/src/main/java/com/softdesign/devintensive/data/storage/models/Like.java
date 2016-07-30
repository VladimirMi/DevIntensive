package com.softdesign.devintensive.data.storage.models;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

@Entity(active = true, nameInDb = "LIKES")
public class Like {
    @Id
    private Long id;

    @NotNull
    private String objectRemoteId;

    @NotNull
    private String subjectRemoteId;

    public Like(String objectRemoteId, String subjectRemoteId) {
        this.objectRemoteId = objectRemoteId;
        this.subjectRemoteId = subjectRemoteId;
    }

    /** Used for active entity operations. */
    @Generated(hash = 1401850954)
    private transient LikeDao myDao;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 935226518)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getLikeDao() : null;
    }

    public String getSubjectRemoteId() {
        return this.subjectRemoteId;
    }

    public void setSubjectRemoteId(String subjectRemoteId) {
        this.subjectRemoteId = subjectRemoteId;
    }

    public String getObjectRemoteId() {
        return this.objectRemoteId;
    }

    public void setObjectRemoteId(String objectRemoteId) {
        this.objectRemoteId = objectRemoteId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 578581680)
    public Like(Long id, @NotNull String objectRemoteId,
            @NotNull String subjectRemoteId) {
        this.id = id;
        this.objectRemoteId = objectRemoteId;
        this.subjectRemoteId = subjectRemoteId;
    }

    @Generated(hash = 763251169)
    public Like() {
    }
}

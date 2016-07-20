package com.softdesign.devintensive.data.storage.models;

import com.softdesign.devintensive.data.network.res.UserListRes;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(active = true, nameInDb = "USERS")
public class User {

    @Id
    private Long id;

    @NotNull
    @Unique
    private String remoteId;

    private String photo;

    @NotNull
    @Unique
    private String fullName;

    @NotNull
    @Unique
    private String searchName;

    private int rating;

    private int codeLines;

    private int projects;

    private String bio;

    private int order;

    private boolean deleted;

    @ToMany(joinProperties = {
            @JoinProperty(name = "remoteId", referencedName = "userRemoteId")
    })
    private List<Repository> repositories;

    public User(UserListRes.UserData userData, int order) {
        this.remoteId = userData.getId();
        this.photo = userData.getPublicInfo().getPhoto();
        this.fullName = userData.getFullName();
        this.searchName = userData.getFullName().toUpperCase();
        this.rating = userData.getProfileValues().getRating();
        this.codeLines = userData.getProfileValues().getCodeLines();
        this.projects = userData.getProfileValues().getProjects();
        this.bio = userData.getPublicInfo().getBio();
        this.order = order;
        this.deleted = false;
    }

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

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 438307964)
    public synchronized void resetRepositories() {
        repositories = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1643807649)
    public List<Repository> getRepositories() {
        if (repositories == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RepositoryDao targetDao = daoSession.getRepositoryDao();
            List<Repository> repositoriesNew = targetDao._queryUser_Repositories(remoteId);
            synchronized (this) {
                if (repositories == null) {
                    repositories = repositoriesNew;
                }
            }
        }
        return repositories;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 2059241980)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1507654846)
    private transient UserDao myDao;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public String getBio() {
        return this.bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getProjects() {
        return this.projects;
    }

    public void setProjects(int projects) {
        this.projects = projects;
    }

    public int getCodeLines() {
        return this.codeLines;
    }

    public void setCodeLines(int codeLines) {
        this.codeLines = codeLines;
    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getSearchName() {
        return this.searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRemoteId() {
        return this.remoteId;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean getDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }


    @Generated(hash = 1815241646)
    public User(Long id, @NotNull String remoteId, String photo, @NotNull String fullName,
            @NotNull String searchName, int rating, int codeLines, int projects, String bio,
            int order, boolean deleted) {
        this.id = id;
        this.remoteId = remoteId;
        this.photo = photo;
        this.fullName = fullName;
        this.searchName = searchName;
        this.rating = rating;
        this.codeLines = codeLines;
        this.projects = projects;
        this.bio = bio;
        this.order = order;
        this.deleted = deleted;
    }

    @Generated(hash = 586692638)
    public User() {
    }

}

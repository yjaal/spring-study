package win.iot4yj.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 定时任务表
 * </p>
 *
 * @author joyang
 * @since 2020-05-24
 */
@TableName("t_quartz_job")
@ApiModel(value="QuartzJob对象", description="定时任务表")
public class QuartzJob extends Model<QuartzJob> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "name", type = IdType.INPUT)
    private String name;

    @ApiModelProperty(value = "定时任务分组")
    private String group;

    @ApiModelProperty(value = "任务类名，一般和任务名相同")
    private String className;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.name;
    }

    @Override
    public String toString() {
        return "QuartzJob{" +
        "name=" + name +
        ", group=" + group +
        ", className=" + className +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}

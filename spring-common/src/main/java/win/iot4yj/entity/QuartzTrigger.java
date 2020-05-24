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
 * 触发器表
 * </p>
 *
 * @author joyang
 * @since 2020-05-24
 */
@TableName("t_quartz_trigger")
@ApiModel(value="QuartzTrigger对象", description="触发器表")
public class QuartzTrigger extends Model<QuartzTrigger> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "触发器名字")
    @TableId(value = "name", type = IdType.INPUT)
    private String name;

    @ApiModelProperty(value = "触发器分组")
    private String group;

    @ApiModelProperty(value = "绑定的定时任务名字")
    private String jobName;

    @ApiModelProperty(value = "触发器状态")
    private String status;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "上次执行时间")
    private String preTime;

    @ApiModelProperty(value = "下次执行时间")
    private String nextTime;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
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

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getPreTime() {
        return preTime;
    }

    public void setPreTime(String preTime) {
        this.preTime = preTime;
    }

    public String getNextTime() {
        return nextTime;
    }

    public void setNextTime(String nextTime) {
        this.nextTime = nextTime;
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
        return "QuartzTrigger{" +
        "name=" + name +
        ", group=" + group +
        ", jobName=" + jobName +
        ", status=" + status +
        ", startTime=" + startTime +
        ", preTime=" + preTime +
        ", nextTime=" + nextTime +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}

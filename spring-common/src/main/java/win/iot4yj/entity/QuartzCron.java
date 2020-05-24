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
 * 触发事件表达式
 * </p>
 *
 * @author joyang
 * @since 2020-05-24
 */
@TableName("t_quartz_cron")
@ApiModel(value="QuartzCron对象", description="触发事件表达式")
public class QuartzCron extends Model<QuartzCron> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "触发器分组")
    @TableId(value = "trigger_group", type = IdType.INPUT)
    private String triggerGroup;

    @ApiModelProperty(value = "表达式")
    private String exp;

    @ApiModelProperty(value = "时区")
    private String zone;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
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
        return this.triggerGroup;
    }

    @Override
    public String toString() {
        return "QuartzCron{" +
        "triggerGroup=" + triggerGroup +
        ", exp=" + exp +
        ", zone=" + zone +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}

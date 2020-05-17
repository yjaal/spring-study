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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 申额日志表
 * </p>
 *
 * @author joyang
 * @since 2020-05-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wfts_fcts_adm_quota_log")
@ApiModel(value="QuotaLog对象", description="申额日志表")
public class QuotaLog extends Model<QuotaLog> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "系统流水号")
    @TableId(value = "sys_seq", type = IdType.INPUT)
    private String sysSeq;

    @ApiModelProperty(value = "业务流水号")
    private String bizSeq;

    @ApiModelProperty(value = "步骤描述")
    private String step;

    @ApiModelProperty(value = "调用Q，如“04205771_01_onlineIDVerify”, 调用cnc则会有服务码")
    private String topic;

    @ApiModelProperty(value = "客户号")
    private String custNo;

    @ApiModelProperty(value = "open id")
    private String openId;

    @ApiModelProperty(value = "union id")
    private String unionId;

    @ApiModelProperty(value = "返回码")
    private String retCode;

    @ApiModelProperty(value = "返回消息")
    private String retMsg;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.sysSeq;
    }

}

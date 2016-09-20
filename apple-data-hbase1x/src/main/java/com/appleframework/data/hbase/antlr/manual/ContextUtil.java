package com.appleframework.data.hbase.antlr.manual;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.hadoop.hbase.filter.Filter;

import com.appleframework.data.hbase.antlr.auto.StatementsParser.CidContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.CidListContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.Constant2Context;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.ConstantContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.DeleteHqlClContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.DeletehqlcContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.InsertHqlClContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.InserthqlcContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.LimitexpContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.MaxversionexpContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.RowkeyrangeContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.SelectCidListContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.SelectHqlClContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.SelecthqlcContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.TsrangeContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.WherecContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.ProgContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.RowkeyexpContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.SelectclContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.TsexpContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.VarContext;
import com.appleframework.data.hbase.antlr.manual.visitor.Constant2Visitor;
import com.appleframework.data.hbase.antlr.manual.visitor.FilterVisitor;
import com.appleframework.data.hbase.antlr.manual.visitor.RowKeyRangeVisitor;
import com.appleframework.data.hbase.antlr.manual.visitor.RowKeyVisitor;
import com.appleframework.data.hbase.antlr.manual.visitor.SelectCidListVisitor;
import com.appleframework.data.hbase.antlr.manual.visitor.TimeStampRangeVisitor;
import com.appleframework.data.hbase.client.QueryExtInfo;
import com.appleframework.data.hbase.client.RowKey;
import com.appleframework.data.hbase.config.HBaseColumnSchema;
import com.appleframework.data.hbase.config.HBaseTableConfig;
import com.appleframework.data.hbase.config.SimpleHbaseConstants;
import com.appleframework.data.hbase.config.SimpleHbaseRuntimeSetting;
import com.appleframework.data.hbase.core.NotNullable;
import com.appleframework.data.hbase.core.Nullable;
import com.appleframework.data.hbase.exception.SimpleHBaseException;
import com.appleframework.data.hbase.util.Util;

/**
 * ContextUtil.
 * 
 * @author xinzhi.zhang
 * */
public class ContextUtil {

    /**
     * Parse HBaseColumnSchema from cidContext.
     * */
    @NotNullable
    public static HBaseColumnSchema parseHBaseColumnSchema(
            @NotNullable HBaseTableConfig hbaseTableConfig,
            @NotNullable CidContext cidContext) {
        Util.checkNull(hbaseTableConfig);
        Util.checkNull(cidContext);

        String cid = cidContext.TEXT().getText();

        String[] parts = cid.toString().split(
                SimpleHbaseConstants.Family_Qualifier_Separator);
        if (parts.length == 1) {
            return hbaseTableConfig.getHbaseTableSchema().findColumnSchema(
                    parts[0]);
        }

        if (parts.length == 2) {
            return hbaseTableConfig.getHbaseTableSchema().findColumnSchema(
                    parts[0], parts[1]);
        }

        throw new SimpleHBaseException("parseHBaseColumnSchema error. cid="
                + cid);
    }

    /**
     * Parse HBaseColumnSchema list from CidListContext.
     * */
    @NotNullable
    public static List<HBaseColumnSchema> parseHBaseColumnSchemaList(
            @NotNullable HBaseTableConfig hbaseTableConfig,
            @NotNullable CidListContext cidListContext) {
        Util.checkNull(hbaseTableConfig);
        Util.checkNull(cidListContext);

        List<HBaseColumnSchema> result = new ArrayList<HBaseColumnSchema>();

        for (CidContext cidContext : cidListContext.cid()) {
            result.add(parseHBaseColumnSchema(hbaseTableConfig, cidContext));
        }

        return result;

    }

    /**
     * Parse HBaseColumnSchema list from SelectCidListContext.
     * */
    @NotNullable
    public static List<HBaseColumnSchema> parseHBaseColumnSchemaList(
            @NotNullable HBaseTableConfig hbaseTableConfig,
            @NotNullable SelectCidListContext selectCidListContext) {
        Util.checkNull(hbaseTableConfig);
        Util.checkNull(selectCidListContext);

        SelectCidListVisitor visitor = new SelectCidListVisitor(
                hbaseTableConfig);
        return selectCidListContext.accept(visitor);
    }

    /**
     * Parse parameter from varContext.
     * */
    @NotNullable
    public static Object parsePara(@NotNullable VarContext varContext,
            @NotNullable Map<String, Object> para) {
        Util.checkNull(varContext);
        Util.checkNull(para);

        String var = varContext.TEXT().getText();
        Util.checkEmptyString(var);

        Object obj = para.get(var);
        Util.checkNull(obj);

        return obj;
    }

    /**
     * Parse parameter list from varContext list.
     * */
    @NotNullable
    public static List<Object> parseParaList(
            @NotNullable List<VarContext> varContextList,
            @NotNullable Map<String, Object> para) {
        Util.checkNull(varContextList);
        Util.checkNull(para);

        List<Object> result = new ArrayList<Object>();
        for (VarContext varContext : varContextList) {
            result.add(parsePara(varContext, para));
        }
        return result;
    }

    /**
     * Parse constant from constant2Context.
     * */
    @Nullable
    public static Object parseConstant2(
            @NotNullable HBaseColumnSchema hbaseColumnSchema,
            @NotNullable Constant2Context constant2Context,
            @NotNullable SimpleHbaseRuntimeSetting runtimeSetting) {
        Util.checkNull(hbaseColumnSchema);
        Util.checkNull(constant2Context);
        Util.checkNull(runtimeSetting);

        Constant2Visitor visitor = new Constant2Visitor(hbaseColumnSchema,
                runtimeSetting);
        return constant2Context.accept(visitor);
    }

    /**
     * Parse constant from constantContext.
     * */
    @NotNullable
    public static Object parseConstant(
            @NotNullable HBaseColumnSchema hbaseColumnSchema,
            @NotNullable ConstantContext constantContext,
            @NotNullable SimpleHbaseRuntimeSetting runtimeSetting) {
        Util.checkNull(hbaseColumnSchema);
        Util.checkNull(constantContext);
        Util.checkNull(runtimeSetting);

        String constant = constantContext.TEXT().getText();
        Util.checkEmptyString(constant);

        Object obj = runtimeSetting.interpret(hbaseColumnSchema.getType(),
                constant);
        Util.checkNull(obj);
        return obj;
    }

    /**
     * Parse constant list from constantContext list.
     * */
    @NotNullable
    public static List<Object> parseConstantList(
            @NotNullable HBaseColumnSchema hbaseColumnSchema,
            @NotNullable List<ConstantContext> constantContextList,
            @NotNullable SimpleHbaseRuntimeSetting runtimeSetting) {
        Util.checkNull(hbaseColumnSchema);
        Util.checkNull(constantContextList);
        Util.checkNull(runtimeSetting);

        List<Object> result = new ArrayList<Object>();
        for (ConstantContext constantContext : constantContextList) {
            result.add(parseConstant(hbaseColumnSchema, constantContext,
                    runtimeSetting));
        }
        return result;
    }

    /**
     * Parse Rowkey from RowkeyexpContext.
     */
    @NotNullable
    public static RowKey parseRowKey(
            @NotNullable RowkeyexpContext rowkeyexpContext,
            @NotNullable SimpleHbaseRuntimeSetting simpleHbaseRuntimeSetting) {
        Util.checkNull(rowkeyexpContext);
        Util.checkNull(simpleHbaseRuntimeSetting);

        RowKeyVisitor visitor = new RowKeyVisitor(simpleHbaseRuntimeSetting);
        RowKey rowkey = rowkeyexpContext.accept(visitor);
        Util.checkNull(rowkey);

        return rowkey;
    }

    /**
     * Parse RowkeyRange from RowkeyrangeContext.
     */
    @NotNullable
    public static RowKeyRange parseRowKeyRange(
            RowkeyrangeContext rowkeyrangeContext,
            @NotNullable SimpleHbaseRuntimeSetting simpleHbaseRuntimeSetting) {
        Util.checkNull(rowkeyrangeContext);
        Util.checkNull(simpleHbaseRuntimeSetting);

        RowKeyRangeVisitor visitor = new RowKeyRangeVisitor(
                simpleHbaseRuntimeSetting);
        RowKeyRange rowkeyRange = rowkeyrangeContext.accept(visitor);

        Util.checkNull(rowkeyRange);
        return rowkeyRange;
    }

    /**
     * Parse Date from TsexpContext.
     */
    @NotNullable
    public static Date parseTimeStampDate(
            @NotNullable TsexpContext tsexpContext,
            @NotNullable SimpleHbaseRuntimeSetting runtimeSetting) {
        Util.checkNull(tsexpContext);
        Util.checkNull(runtimeSetting);

        String constant = tsexpContext.constant().TEXT().getText();
        Date date = (Date) runtimeSetting.interpret(Date.class, constant);

        Util.checkNull(date);
        return date;
    }

    /**
     * Parse TimeStampRange from tsrangeContext.
     */
    @NotNullable
    public static TimeStampRange parseTimeStampRange(
            @NotNullable TsrangeContext tsrangeContext,
            @NotNullable SimpleHbaseRuntimeSetting runtimeSetting) {
        Util.checkNull(tsrangeContext);
        Util.checkNull(runtimeSetting);

        TimeStampRangeVisitor visitor = new TimeStampRangeVisitor(
                runtimeSetting);
        TimeStampRange timeStampRange = tsrangeContext.accept(visitor);

        Util.checkNull(timeStampRange);
        return timeStampRange;
    }

    /**
     * Parse filter from select hql's progContext.
     * */
    @Nullable
    public static Filter parseSelectFilter(
            @NotNullable ProgContext progContext,
            @NotNullable HBaseTableConfig hbaseTableConfig,
            @Nullable Map<String, Object> para,
            @NotNullable SimpleHbaseRuntimeSetting runtimeSetting) {
        Util.checkNull(progContext);
        Util.checkNull(hbaseTableConfig);
        Util.checkNull(runtimeSetting);

        SelectclContext selectclContext = ((SelectclContext) progContext);
        return parseFilter(selectclContext.selectc().wherec(),
                hbaseTableConfig, para, runtimeSetting);
    }

    /**
     * Parse filter from ConditioncContext.
     * */
    @Nullable
    public static Filter parseFilter(@Nullable WherecContext wherecContext,
            @NotNullable HBaseTableConfig hbaseTableConfig,
            @NotNullable SimpleHbaseRuntimeSetting runtimeSetting) {
        Util.checkNull(hbaseTableConfig);
        Util.checkNull(runtimeSetting);

        return parseFilter(wherecContext, hbaseTableConfig, null,
                runtimeSetting);
    }

    /**
     * Parse filter from ConditioncContext.
     * */
    @Nullable
    private static Filter parseFilter(@Nullable WherecContext wherecContext,
            @NotNullable HBaseTableConfig hbaseTableConfig,
            @Nullable Map<String, Object> para,
            @NotNullable SimpleHbaseRuntimeSetting runtimeSetting) {

        Util.checkNull(hbaseTableConfig);
        Util.checkNull(runtimeSetting);

        if (wherecContext == null)
            return null;
        if (wherecContext.conditionc() == null)
            return null;

        FilterVisitor visitor = new FilterVisitor(hbaseTableConfig, para,
                runtimeSetting);
        return wherecContext.conditionc().accept(visitor);
    }

    /**
     * Parse InserthqlcContext from insert hql.
     * */
    @NotNullable
    public static InserthqlcContext parseInserthqlcContext(
            @NotNullable ProgContext progContext) {
        Util.checkNull(progContext);

        InsertHqlClContext insertHqlClContext = (InsertHqlClContext) progContext;
        InserthqlcContext result = insertHqlClContext.inserthqlc();

        Util.checkNull(result);
        return result;
    }

    /**
     * Parse InserthqlcContext from insert hql.
     * */
    @NotNullable
    public static SelecthqlcContext parseSelecthqlcContext(
            @NotNullable ProgContext progContext) {
        Util.checkNull(progContext);

        SelectHqlClContext selectHqlClContext = (SelectHqlClContext) progContext;
        SelecthqlcContext result = selectHqlClContext.selecthqlc();

        Util.checkNull(result);
        return result;
    }

    /**
     * Parse DeletehqlcContext from delete hql.
     * */
    @NotNullable
    public static DeletehqlcContext parseDeletehqlcContext(
            @NotNullable ProgContext progContext) {
        Util.checkNull(progContext);

        DeleteHqlClContext deleteHqlClContext = (DeleteHqlClContext) progContext;
        DeletehqlcContext result = deleteHqlClContext.deletehqlc();

        Util.checkNull(result);
        return result;
    }

    /**
     * Parse QueryExtInfo from SelecthqlcContext.
     * */
    @NotNullable
    public static QueryExtInfo parseQueryExtInfo(
            @NotNullable SelecthqlcContext selecthqlcContext,
            @NotNullable SimpleHbaseRuntimeSetting runtimeSetting) {
        Util.checkNull(selecthqlcContext);
        Util.checkNull(runtimeSetting);

        QueryExtInfo extInfo = new QueryExtInfo();

        MaxversionexpContext maxversionexpContext = selecthqlcContext
                .maxversionexp();
        if (maxversionexpContext != null) {
            extInfo.setMaxVersions(Integer.parseInt(maxversionexpContext
                    .maxversion().TEXT().getText()));
        }

        TsrangeContext tsrangeContext = selecthqlcContext.tsrange();
        if (tsrangeContext != null) {
            TimeStampRange timeStampRange = parseTimeStampRange(tsrangeContext,
                    runtimeSetting);
            Date minStamp = timeStampRange.getStart();
            Date maxStamp = timeStampRange.getEnd();
            extInfo.setTimeRange(minStamp, maxStamp);
        }

        LimitexpContext limitexpContext = selecthqlcContext.limitexp();
        if (limitexpContext != null) {
            List<TerminalNode> terminalNodeList = limitexpContext.TEXT();
            Util.check(terminalNodeList.size() == 1
                    || terminalNodeList.size() == 2);
            if (terminalNodeList.size() == 1) {
                extInfo.setLimit(0L,
                        Long.parseLong(terminalNodeList.get(0).getText()));
            }

            if (terminalNodeList.size() == 2) {
                extInfo.setLimit(
                        Long.parseLong(terminalNodeList.get(0).getText()),
                        Long.parseLong(terminalNodeList.get(1).getText()));
            }
        }

        return extInfo;
    }

    private ContextUtil() {
    }
}

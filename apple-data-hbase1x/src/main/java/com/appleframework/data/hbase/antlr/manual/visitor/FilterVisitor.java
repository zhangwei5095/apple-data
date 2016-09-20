package com.appleframework.data.hbase.antlr.manual.visitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;

import com.appleframework.data.hbase.antlr.auto.StatementsBaseVisitor;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.AndconditionContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.BetweenconstantContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.BetweenvarContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.CidContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.ConditioncContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.ConstantContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.ConstantListContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.EqualconstantContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.EqualvarContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.GreaterconstantContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.GreaterequalconstantContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.GreaterequalvarContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.GreatervarContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.InconstantlistContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.ConditionwrapperContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.InvarlistContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.IsmissingcContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.IsnotmissingcContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.IsnotnullcContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.IsnullcContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.LessconstantContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.LessequalconstantContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.LessequalvarContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.LessvarContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.MatchconstantContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.MatchvarContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.NotbetweenconstantContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.NotbetweenvarContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.NotequalconstantContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.NotequalvarContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.NotinconstantlistContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.NotinvarlistContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.NotmatchconstantContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.NotmatchvarContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.OrconditionContext;
import com.appleframework.data.hbase.antlr.auto.StatementsParser.VarContext;
import com.appleframework.data.hbase.antlr.manual.ContextUtil;
import com.appleframework.data.hbase.config.HBaseColumnSchema;
import com.appleframework.data.hbase.config.HBaseTableConfig;
import com.appleframework.data.hbase.config.SimpleHbaseRuntimeSetting;
import com.appleframework.data.hbase.exception.SimpleHBaseException;
import com.appleframework.data.hbase.util.BytesUtil;
import com.appleframework.data.hbase.util.Util;

/**
 * FilterVisitor.
 * 
 * @author xinzhi.zhang
 * */
public class FilterVisitor extends StatementsBaseVisitor<Filter> {

    private HBaseTableConfig          hbaseTableConfig;
    private Map<String, Object>       para;
    private SimpleHbaseRuntimeSetting runtimeSetting;

    public FilterVisitor(HBaseTableConfig hbaseTableConfig,
            Map<String, Object> para, SimpleHbaseRuntimeSetting runtimeSetting) {
        this.hbaseTableConfig = hbaseTableConfig;
        this.para = para;
        this.runtimeSetting = runtimeSetting;
    }

    @Override
    public Filter visitOrcondition(OrconditionContext ctx) {
        List<ConditioncContext> conditioncContextList = ctx.conditionc();
        List<Filter> filters = new ArrayList<Filter>();
        for (ConditioncContext conditioncContext : conditioncContextList) {
            filters.add(conditioncContext.accept(this));
        }

        FilterList filterList = new FilterList(Operator.MUST_PASS_ONE, filters);
        return filterList;
    }

    @Override
    public Filter visitAndcondition(AndconditionContext ctx) {
        List<ConditioncContext> conditioncContextList = ctx.conditionc();
        List<Filter> filters = new ArrayList<Filter>();
        for (ConditioncContext conditioncContext : conditioncContextList) {
            filters.add(conditioncContext.accept(this));
        }

        FilterList filterList = new FilterList(Operator.MUST_PASS_ALL, filters);
        return filterList;
    }

    @Override
    public Filter visitConditionwrapper(ConditionwrapperContext ctx) {
        return ctx.conditionc().accept(this);
    }

    @Override
    public Filter visitEqualvar(EqualvarContext ctx) {
        CidContext cidContext = ctx.cid();
        VarContext varContext = ctx.var();
        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);
        Object object = ContextUtil.parsePara(varContext, para);

        return constructFilter(hbaseColumnSchema, CompareOp.EQUAL, object);
    }

    @Override
    public Filter visitEqualconstant(EqualconstantContext ctx) {
        CidContext cidContext = ctx.cid();
        ConstantContext constantContext = ctx.constant();

        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);
        Object object = ContextUtil.parseConstant(hbaseColumnSchema,
                constantContext, runtimeSetting);

        return constructFilter(hbaseColumnSchema, CompareOp.EQUAL, object);
    }

    @Override
    public Filter visitIsnullc(IsnullcContext ctx) {
        CidContext cidContext = ctx.cid();
        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);
        return constructFilter(hbaseColumnSchema, CompareOp.EQUAL,
                BytesUtil.EMPTY, true);
    }

    @Override
    public Filter visitIsnotnullc(IsnotnullcContext ctx) {
        CidContext cidContext = ctx.cid();
        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);
        return constructFilter(hbaseColumnSchema, CompareOp.NOT_EQUAL,
                BytesUtil.EMPTY, true);
    }

    @Override
    public Filter visitNotequalconstant(NotequalconstantContext ctx) {
        CidContext cidContext = ctx.cid();
        ConstantContext constantContext = ctx.constant();

        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);
        Object object = ContextUtil.parseConstant(hbaseColumnSchema,
                constantContext, runtimeSetting);

        return constructFilter(hbaseColumnSchema, CompareOp.NOT_EQUAL, object);
    }

    @Override
    public Filter visitNotequalvar(NotequalvarContext ctx) {
        CidContext cidContext = ctx.cid();
        VarContext varContext = ctx.var();
        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);
        Object object = ContextUtil.parsePara(varContext, para);

        return constructFilter(hbaseColumnSchema, CompareOp.NOT_EQUAL, object);
    }

    @Override
    public Filter visitLessvar(LessvarContext ctx) {

        CidContext cidContext = ctx.cid();
        VarContext varContext = ctx.var();

        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);
        Object object = ContextUtil.parsePara(varContext, para);

        return constructFilter(hbaseColumnSchema, CompareOp.LESS, object);
    }

    @Override
    public Filter visitLessconstant(LessconstantContext ctx) {
        CidContext cidContext = ctx.cid();
        ConstantContext constantContext = ctx.constant();

        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);
        Object object = ContextUtil.parseConstant(hbaseColumnSchema,
                constantContext, runtimeSetting);

        return constructFilter(hbaseColumnSchema, CompareOp.LESS, object);

    }

    @Override
    public Filter visitLessequalconstant(LessequalconstantContext ctx) {
        CidContext cidContext = ctx.cid();
        ConstantContext constantContext = ctx.constant();

        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);
        Object object = ContextUtil.parseConstant(hbaseColumnSchema,
                constantContext, runtimeSetting);

        return constructFilter(hbaseColumnSchema, CompareOp.LESS_OR_EQUAL,
                object);
    }

    @Override
    public Filter visitLessequalvar(LessequalvarContext ctx) {
        CidContext cidContext = ctx.cid();
        VarContext varContext = ctx.var();

        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);
        Object object = ContextUtil.parsePara(varContext, para);
        return constructFilter(hbaseColumnSchema, CompareOp.LESS_OR_EQUAL,
                object);
    }

    @Override
    public Filter visitGreaterconstant(GreaterconstantContext ctx) {
        CidContext cidContext = ctx.cid();
        ConstantContext constantContext = ctx.constant();

        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);
        Object object = ContextUtil.parseConstant(hbaseColumnSchema,
                constantContext, runtimeSetting);

        return constructFilter(hbaseColumnSchema, CompareOp.GREATER, object);
    }

    @Override
    public Filter visitGreatervar(GreatervarContext ctx) {
        CidContext cidContext = ctx.cid();
        VarContext varContext = ctx.var();

        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);
        Object object = ContextUtil.parsePara(varContext, para);
        return constructFilter(hbaseColumnSchema, CompareOp.GREATER, object);
    }

    @Override
    public Filter visitGreaterequalvar(GreaterequalvarContext ctx) {
        CidContext cidContext = ctx.cid();
        VarContext varContext = ctx.var();

        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);
        Object object = ContextUtil.parsePara(varContext, para);
        return constructFilter(hbaseColumnSchema, CompareOp.GREATER_OR_EQUAL,
                object);
    }

    @Override
    public Filter visitGreaterequalconstant(GreaterequalconstantContext ctx) {
        CidContext cidContext = ctx.cid();
        ConstantContext constantContext = ctx.constant();

        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);
        Object object = ContextUtil.parseConstant(hbaseColumnSchema,
                constantContext, runtimeSetting);

        return constructFilter(hbaseColumnSchema, CompareOp.GREATER_OR_EQUAL,
                object);
    }

    private static Filter constructFilter(HBaseColumnSchema hbaseColumnSchema,
            CompareOp compareOp, Object object) {
        Util.checkNull(hbaseColumnSchema);

        byte[] value = hbaseColumnSchema.getTypeHandler().toBytes(
                hbaseColumnSchema.getType(), object);
        return constructFilter(hbaseColumnSchema, compareOp, value, true);
    }

    private static Filter constructFilter(HBaseColumnSchema hbaseColumnSchema,
            CompareOp compareOp, byte[] value, boolean filterIfMissing) {
        Util.checkNull(hbaseColumnSchema);
        Util.checkNull(compareOp);
        Util.checkNull(value);

        byte[] familyBytes = hbaseColumnSchema.getFamilyBytes();
        byte[] qualifierBytes = hbaseColumnSchema.getQualifierBytes();

        SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter(
                familyBytes, qualifierBytes, compareOp, value);
        singleColumnValueFilter.setFilterIfMissing(filterIfMissing);

        return singleColumnValueFilter;

    }

    @Override
    public Filter visitIsnotmissingc(IsnotmissingcContext ctx) {
        CidContext cidContext = ctx.cid();
        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);
        return constructFilter(hbaseColumnSchema, CompareOp.GREATER_OR_EQUAL,
                BytesUtil.EMPTY, true);
    }

    @Override
    public Filter visitIsmissingc(IsmissingcContext ctx) {
        CidContext cidContext = ctx.cid();
        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);
        return constructFilter(hbaseColumnSchema, CompareOp.LESS,
                BytesUtil.EMPTY, false);
    }

    @Override
    public Filter visitNotmatchconstant(NotmatchconstantContext ctx) {
        CidContext cidContext = ctx.cid();
        ConstantContext constantContext = ctx.constant();

        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);
        Object object = ContextUtil.parseConstant(hbaseColumnSchema,
                constantContext, runtimeSetting);

        return constructFilterWithRegex(hbaseColumnSchema, CompareOp.NOT_EQUAL,
                object);
    }

    @Override
    public Filter visitNotmatchvar(NotmatchvarContext ctx) {
        CidContext cidContext = ctx.cid();
        VarContext varContext = ctx.var();

        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);
        Object object = ContextUtil.parsePara(varContext, para);

        return constructFilterWithRegex(hbaseColumnSchema, CompareOp.NOT_EQUAL,
                object);
    }

    @Override
    public Filter visitMatchvar(MatchvarContext ctx) {
        CidContext cidContext = ctx.cid();
        VarContext varContext = ctx.var();

        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);
        Object object = ContextUtil.parsePara(varContext, para);

        return constructFilterWithRegex(hbaseColumnSchema, CompareOp.EQUAL,
                object);
    }

    @Override
    public Filter visitMatchconstant(MatchconstantContext ctx) {
        CidContext cidContext = ctx.cid();
        ConstantContext constantContext = ctx.constant();

        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);
        Object object = ContextUtil.parseConstant(hbaseColumnSchema,
                constantContext, runtimeSetting);

        return constructFilterWithRegex(hbaseColumnSchema, CompareOp.EQUAL,
                object);
    }

    private static Filter constructFilterWithRegex(
            HBaseColumnSchema hbaseColumnSchema, CompareOp compareOp,
            Object object) {
        Util.checkNull(hbaseColumnSchema);
        Util.checkNull(compareOp);
        Util.checkNull(object);

        if (compareOp != CompareOp.EQUAL && compareOp != CompareOp.NOT_EQUAL) {
            throw new SimpleHBaseException(
                    "only EQUAL or NOT_EQUAL can use regex match. compareOp = "
                            + compareOp);
        }
        if (object.getClass() != String.class) {
            throw new SimpleHBaseException(
                    "only String can use regex match. object = " + object);
        }
        if (hbaseColumnSchema.getType() != String.class) {
            throw new SimpleHBaseException(
                    "only String can use regex match. hbaseColumnSchema = "
                            + hbaseColumnSchema);
        }

        byte[] familyBytes = hbaseColumnSchema.getFamilyBytes();
        byte[] qualifierBytes = hbaseColumnSchema.getQualifierBytes();

        RegexStringComparator regexStringComparator = new RegexStringComparator(
                (String) object);

        SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter(
                familyBytes, qualifierBytes, compareOp, regexStringComparator);
        singleColumnValueFilter.setFilterIfMissing(true);

        return singleColumnValueFilter;
    }

    @Override
    public Filter visitNotinconstantlist(NotinconstantlistContext ctx) {
        CidContext cidContext = ctx.cid();
        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);

        ConstantListContext constantListContext = ctx.constantList();
        List<ConstantContext> constantContextList = constantListContext
                .constant();
        List<Object> list = ContextUtil.parseConstantList(hbaseColumnSchema,
                constantContextList, runtimeSetting);

        return constructFilterForContain(hbaseColumnSchema,
                CompareOp.NOT_EQUAL, list, Operator.MUST_PASS_ALL);
    }

    @SuppressWarnings("unchecked")
	@Override
    public Filter visitNotinvarlist(NotinvarlistContext ctx) {
        CidContext cidContext = ctx.cid();
        VarContext varContext = ctx.var();

        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);
        Object object = ContextUtil.parsePara(varContext, para);

        return constructFilterForContain(hbaseColumnSchema,
                CompareOp.NOT_EQUAL, (List<Object>) object,
                Operator.MUST_PASS_ALL);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Filter visitInvarlist(InvarlistContext ctx) {
        CidContext cidContext = ctx.cid();
        VarContext varContext = ctx.var();

        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);
        Object object = ContextUtil.parsePara(varContext, para);

        return constructFilterForContain(hbaseColumnSchema, CompareOp.EQUAL,
                (List<Object>) object, Operator.MUST_PASS_ONE);
    }

    @Override
    public Filter visitInconstantlist(InconstantlistContext ctx) {
        CidContext cidContext = ctx.cid();
        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);

        ConstantListContext constantListContext = ctx.constantList();
        List<ConstantContext> constantContextList = constantListContext
                .constant();
        List<Object> list = ContextUtil.parseConstantList(hbaseColumnSchema,
                constantContextList, runtimeSetting);

        return constructFilterForContain(hbaseColumnSchema, CompareOp.EQUAL,
                list, Operator.MUST_PASS_ONE);
    }

    private static Filter constructFilterForContain(
            HBaseColumnSchema hbaseColumnSchema, CompareOp compareOp,
            List<Object> list, Operator operator) {
        Util.checkNull(hbaseColumnSchema);
        Util.checkNull(compareOp);
        Util.checkNull(list);
        Util.checkNull(operator);

        List<Filter> filters = new ArrayList<Filter>();
        for (Object obj : list) {
            filters.add(constructFilter(hbaseColumnSchema, compareOp, obj));
        }

        FilterList filterList = new FilterList(operator, filters);
        return filterList;
    }

    @Override
    public Filter visitNotbetweenconstant(NotbetweenconstantContext ctx) {
        CidContext cidContext = ctx.cid();
        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);

        List<ConstantContext> constantContextList = ctx.constant();

        List<Object> list = ContextUtil.parseConstantList(hbaseColumnSchema,
                constantContextList, runtimeSetting);

        Filter startFilter = constructFilter(hbaseColumnSchema, CompareOp.LESS,
                list.get(0));
        Filter endFilter = constructFilter(hbaseColumnSchema,
                CompareOp.GREATER, list.get(1));

        FilterList filterList = new FilterList(Operator.MUST_PASS_ONE,
                Arrays.asList(startFilter, endFilter));
        return filterList;
    }

    @Override
    public Filter visitNotbetweenvar(NotbetweenvarContext ctx) {
        CidContext cidContext = ctx.cid();
        List<VarContext> varContextList = ctx.var();

        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);
        List<Object> list = ContextUtil.parseParaList(varContextList, para);

        Filter startFilter = constructFilter(hbaseColumnSchema, CompareOp.LESS,
                list.get(0));
        Filter endFilter = constructFilter(hbaseColumnSchema,
                CompareOp.GREATER, list.get(1));

        FilterList filterList = new FilterList(Operator.MUST_PASS_ONE,
                Arrays.asList(startFilter, endFilter));

        return filterList;
    }

    @Override
    public Filter visitBetweenvar(BetweenvarContext ctx) {
        CidContext cidContext = ctx.cid();
        List<VarContext> varContextList = ctx.var();

        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);
        List<Object> list = ContextUtil.parseParaList(varContextList, para);

        Filter startFilter = constructFilter(hbaseColumnSchema,
                CompareOp.GREATER_OR_EQUAL, list.get(0));
        Filter endFilter = constructFilter(hbaseColumnSchema,
                CompareOp.LESS_OR_EQUAL, list.get(1));

        FilterList filterList = new FilterList(Operator.MUST_PASS_ALL,
                Arrays.asList(startFilter, endFilter));
        return filterList;
    }

    @Override
    public Filter visitBetweenconstant(BetweenconstantContext ctx) {
        CidContext cidContext = ctx.cid();
        HBaseColumnSchema hbaseColumnSchema = ContextUtil
                .parseHBaseColumnSchema(hbaseTableConfig, cidContext);

        List<ConstantContext> constantContextList = ctx.constant();

        List<Object> list = ContextUtil.parseConstantList(hbaseColumnSchema,
                constantContextList, runtimeSetting);

        Filter startFilter = constructFilter(hbaseColumnSchema,
                CompareOp.GREATER_OR_EQUAL, list.get(0));
        Filter endFilter = constructFilter(hbaseColumnSchema,
                CompareOp.LESS_OR_EQUAL, list.get(1));

        FilterList filterList = new FilterList(Operator.MUST_PASS_ALL,
                Arrays.asList(startFilter, endFilter));

        return filterList;
    }
}

package com.hq.es;


import com.hq.es.pojo.Goods;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.SimpleQueryStringBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.MaxAggregationBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;

import java.io.IOException;

@SpringBootTest
public class ElasticsearchIndexTest {
    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;

    String data = "[{\"labelName\":\"国企\",\"labelDesc\":\"国有企业，是指国务院和地方人民政府分别代表国家履行出资人职责的国有独资企业、国有独资公司以及国有资本控股公司，包括中央和地方国有资产监督管理机构和其他部门所监管的企业本级及其逐级投资形成的企业。\",\"source\":\"天眼查\",\"orderBy\":1,\"id\":\"1-3SKLS44J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"央企\",\"labelDesc\":\"为“中央管理企业”的简称，是指由中央人民政府（国务院）或委托国有资产监督管理机构行使出资人职责，领导班子由中央直接管理或委托中央组织部、国务院国资委或其他等中央部委（协会）管理的国有独资或国有控股企业。\",\"source\":\"天眼查\",\"orderBy\":2,\"id\":\"1-3SKLS29J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"事业单位\",\"labelDesc\":\"是指国家为了社会公益目的，由国家机关举办或者其他组织利用国有资产举办的，从事教育、科技、文化、卫生等活动的社会服务组织。 事业单位接受政府领导，是表现形式为组织或机构的法人实体。 \",\"source\":\"天眼查\",\"orderBy\":3,\"id\":\"1-3SKLS58J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"国家级龙头企业\",\"labelDesc\":\"国家级龙头企业是指在某个行业中，对同行业的其他企业具有很深的影响、号召力和一定的示范、引导作用，并对该地区、该行业或者国家做出突出贡献的企业。重点龙头企业（国家级）的标准：一是我国东部地区的企业固定资产达5000万元以上；近3年销售额在2亿元以上；产地批发市场年交易额在5亿元以上。二是经济效益好，企业资产负债率小于60%；产品转化增值能力强，银行信用等级在A级以上（含A级），有抵御市场风险的能力。三是带动能力强，产加销各环节利益联结机制健全，能带动较多农户；有稳定的较大规模的原料生产基地。四是产品具有市场竞争优势。重点龙头企业应建成管理科学、设备先进、技术力量雄厚的现代企业，成为加工的龙头、市场的中介、服务的中心。\",\"source\":\"天眼查\",\"orderBy\":4,\"id\":\"1-3SKLS52J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"民营科技企业\",\"labelDesc\":\"民营科技企业是指以科技人员为主体创办的，实行自筹资金、自愿组合、自主经营、自负盈亏、自我约束、自我发展的经营机制，主要从事科技成果转化及技术开发、技术转让、技术咨询、技术服务或实行高新技术及其产品的研究、开发、生产、销售的智力、技术密集型的经济实体。\",\"source\":\"天眼查\",\"orderBy\":5,\"id\":\"1-3SKLS57J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"独角兽企业\",\"labelDesc\":\"独角兽企业，是投资行业尤其是风险投资业的术语，一般指成立时间不超过10年、估值超过10亿美元的未上市创业公司。\",\"source\":\"天眼查\",\"orderBy\":6,\"id\":\"1-3SKLS4J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"高新技术企业\",\"labelDesc\":\"高新技术企业是指国家扶持的高新技术领域的技术成果的持续研究开发和转化。换句话说，高新技术企业是利用“国家扶持的高新技术领域”的技术生产（服务）的居民并符合其他认证条件，经专家鉴定合格并在有效期内。\",\"source\":\"天眼查\",\"orderBy\":7,\"id\":\"1-3SKLS6J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"技术创新示范企业\",\"labelDesc\":\"技术创新示范企业(以下简称示范企业）是指工业主要产业中技术创新能力较强、创新业绩显著、具有重要示范和导向作用的企业。技术创新示范企业申报条件:(一）具有独立法人资格，财务管理制度健全，会计信用、纳税信用和银行信用良好;(二）在国内建有科研、生产基地且中方拥有控制权;(三）已认定为省级以上企业技术中心的企业;(四）技术创新成果通过实施技术改造，取得了较显著的成效;(五)具有一定的生产经营规模，从业人员300人以上，年销售收入3000万元以上，资产总额4000万元以上。\",\"source\":\"天眼查\",\"orderBy\":8,\"id\":\"1-3SKLS41J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"牛羚企业\",\"labelDesc\":\"牛羚企业是指具有自主知识产权，连续两年销售收入年均增长30%以上，且最近一个会计年度达500万元人民币以上的企业。这类企业像牛羚一样有强大的生命力，克服重重困难，顽强成长向前狂奔。\",\"source\":\"天眼查\",\"orderBy\":9,\"id\":\"1-3SKLS64J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"雏鹰企业\",\"labelDesc\":\"雏鹰企业是指注册时间不超过10年，具有较强创新能力，在某一细分领域取得突破，未来具有较高发展潜力，得到市场认可的创新型企业。\",\"source\":\"天眼查\",\"orderBy\":10,\"id\":\"1-3SKLS10J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"国家级专精特新小巨人\",\"labelDesc\":\"（1）主要指代那些集中于新一代信息技术、高端装备制造、新能源、新材料、生物医药等中高端产业领域的尚处发展早期的小型企业，它们始终坚持专业化发展战略，普遍具有经营业绩良好、有科技含量高、设备工艺先进、管理体系完善、市场竞争力强等特点。（2）并且极具发展潜力与成长性，有望在未来成为相关领域国际领先的企业。\",\"source\":\"天眼查\",\"orderBy\":11,\"id\":\"1-3SKLS19J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"隐形冠军企业\",\"labelDesc\":\"隐形冠军企业，最早由德国管理学家赫尔曼·西蒙提出，是指那些不为公众所熟知，却在某个细分行业或市场占据领先地位，拥有核心竞争力和明确战略，其产品、服务难以被超越和模仿的中小型企业。\",\"source\":\"天眼查\",\"orderBy\":12,\"id\":\"1-3SKLS22J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"科技小巨人企业\",\"labelDesc\":\"科技创新小巨人企业是指企业在研究、开发、生产、销售和管理过程中，通过技术创新、管理创新、服务创新或模式创新取得核心竞争力，提供高新技术产品或服务，具有较高成长性或发展潜力巨大的科技创新中小企业。\",\"source\":\"天眼查\",\"orderBy\":13,\"id\":\"1-3SKLS55J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"瞪羚企业\",\"labelDesc\":\"创业后跨过死亡谷以科技创新或商业模式创新为支撑进入高成长期的中小企业。按照硅谷的解释，“瞪羚企业”就是高成长型企业，它们具有与“瞪羚”共同的特征——个头不大、跑得快、跳得高，这些企业不仅年增长速度能轻易超越一倍、十倍、百倍、千倍以上，还能迅速实现IPO。认定范围主要是产业领域符合国家和省战略新兴产业发展方向，涵盖新兴工业、新一代信息技术（含大数据、物联网与云计算、高端软件、互联网）、生物健康、人工智能、金融科技、节能环保、消费升级等领域。\",\"source\":\"天眼查\",\"orderBy\":14,\"id\":\"1-3SKLS26J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"企业技术中心\",\"labelDesc\":\"对企业自己的技术中心进行的认证,若是一个企业拥有认证过的技术中心,那就代表企业的研发能力已经上升到一个新的水平,技术荣誉度十分高。\",\"source\":\"天眼查\",\"orderBy\":15,\"id\":\"1-3SKLS17J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"火炬计划项目企业\",\"labelDesc\":\"火炬计划项目企业是实施火炬计划项目的载体。每年从承担火炬计划项目的企业中，择优选定一批重点高新技术企业（集团），国家和地方共同在市场、信息、资金、管理、服务等方面给予支持，促进地方区域经济的发展。\",\"source\":\"天眼查\",\"orderBy\":16,\"id\":\"1-3SKLS18J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"科技型初创企业\",\"labelDesc\":\"1.在中国境内（不包括港、澳、台地区）注册成立、实行查账征收的居民企业；2.接受投资时，从业人数不超过200人，其中具有大学本科以上学历的从业人数不低于30%；资产总额和年销售收入均不超过3000万元；3.接受投资时设立时间不超过5年（60个月）；4.接受投资时以及接受投资后2年内未在境内外证券交易所上市；5.接受投资当年及下一纳税年度，研发费用总额占成本费用支出的比例不低于20%。\",\"source\":\"天眼查\",\"orderBy\":17,\"id\":\"1-3SKLS13J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"技术先进型服务企业\",\"labelDesc\":\"技术先进型服务企业是省级科技部门会同本级商务、财政、税务和发展改革部门根据本通知规定制定本省（自治区、直辖市、计划单列市）技术先进型服务企业认定管理办法，并负责本地区技术先进型服务企业的认定管理工作。\",\"source\":\"天眼查\",\"orderBy\":18,\"id\":\"1-3SKLS15J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"科技型中小企业\",\"labelDesc\":\"“科技型中小企业”是一类有新的规定条件标准的企业。同时符合4项基本条件和1项附加条件的企业可直接确认为科技型中小企业。基本条件：需同时满足。1. 在中国境内（不包括港、澳、台地区）注册的居民企业。2. 职工总数不超过500人、年销售收入不超过2亿元、资产总额不超过2亿元。3. 企业提供的产品和服务不属于国家规定的禁止、限制和淘汰类。4. 企业在填报上一年及当年内未发生重大安全、重大质量事故和严重环境违法、科研严重失信行为，且企业未列入经营异常名录和严重违法失信企业名单。\",\"source\":\"天眼查\",\"orderBy\":19,\"id\":\"1-3SKLS16J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"小微企业\",\"labelDesc\":\"小微企业是小型企业、微型企业、家庭作坊式企业、个体工商户的统称。根据不同的行业进行划型，如农、林、牧、渔业；工业；建筑业；批发业；零售业等，其中农、林、牧、渔业营业收入2000万元以下的为中小微型企业，工业从业人员1000人以下或营业收入40000万元以下的为中小微型企业。 \",\"source\":\"天眼查\",\"orderBy\":20,\"id\":\"1-3SKLS23J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"省级龙头企业\",\"labelDesc\":\"是指在行业内具有较大的经营规模和盈利能力，管理技术水平先进，服务质量和信誉良好，创新能力较强，能对服务业发展和进步起到示范带动作用的企业。围绕我省服务业产业规划和布局，重点在大旅游、大健康、大数据、现代金融、现代物流、现代商贸、科技研发、文化产业、养老服务、会展服务等服务业领域，有计划、有重点培育发展一批省级服务业龙头企业。\",\"source\":\"天眼查\",\"orderBy\":22,\"id\":\"1-3SKLS20J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"省级专精特新小巨人\",\"labelDesc\":\"“小巨人”企业是指业绩良好，发展潜力和培育价值处于成长初期的专精特新小企业，通过培育推动其健康成长，最终成为行业中或本区域的“巨人”。专精特新小巨人企业是专精特新企业中的佼佼者，是专注于细分市场、创新能力强、市场占有率高、掌握关键核心技术、质量效益优的排头兵企业。\",\"source\":\"天眼查\",\"orderBy\":23,\"id\":\"1-3SKLS66J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"中小企协AAA信用\",\"labelDesc\":\"中小AAA企业信用认证〈企业信用评级）是企业信用特征的集中体现，获得3A证书意味着企业的产品质量、管理水平和信誉程度均为国际公认。3A证书是企业在资本市场的通行证。适用于各工商企业、制造业企业和流通企业、建筑安装房地产开发与旅游企业、金融企业等。\",\"source\":\"天眼查\",\"orderBy\":24,\"id\":\"1-3SKLS50J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"科技企业孵化器\",\"labelDesc\":\"科技企业孵化器是培育和扶植高新技术中小企业的服务机构。孵化器通过为新创办的科技型中小企业提供物理空间和基础设施，提供一系列服务支持，降低创业者的创业风险和创业成本，提高创业成功率，促进科技成果转化，帮助和支持科技型中小企业成长与发展，培养成功的企业和企业家。它对推动高新技术产业发展，完善国家和区域创新体系、繁荣经济，发挥着重要的作用，具有重大的社会经济意义。\",\"source\":\"天眼查\",\"orderBy\":25,\"id\":\"1-3SKLS45J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"中关村高新技术企业\",\"labelDesc\":\"在中关村科技园区国家重点支持的高新技术领域，持续进行研究开发与技术成果转化，形成企业核心自主知识产权，并以此为基础开展经营活动，将重大高新技术成果转化成生产力的技术水平，属于国内领先或国际先进的企业。\",\"source\":\"天眼查\",\"orderBy\":26,\"id\":\"1-3SKLS33J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"中关村雏鹰人才企业\",\"labelDesc\":\"1.自企业注册之日起一直担任企业第一大股东，且在企业注册时年龄≤30周岁。2.企业在中关村示范区内注册，且成立时间为1年以上3年以内。3.企业为国家高新技术企业或中关村高新技术企业。4.企业技术创新性好，其中拥有发明专利的优先支持。5.企业市场潜力大，发展前景好，具有良好社会效益。\",\"source\":\"天眼查\",\"orderBy\":27,\"id\":\"1-3SKLS30J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"江苏省民营科技企业\",\"labelDesc\":\"根据江苏省民营科技企业协会苏民科协[2018]3号文件需具备以下条件的企业：（一）经工商行政管理部门登记注册并经营的非外商控股的企业；（二）由公民、法人或其他组织自筹资金、自愿结合、自主经营、自负盈亏；（三）符合国家产业政策、技术政策及其发展方向，主要从事技术开发、技术转让、技术咨询、技术服务以及新产品研究、开发、生产、经营业务；（四）从业人员中科技人员占总人数的20%以上；（五）拥有专利或者专有技术等相关知识产权证明材料；（六）企业技术性收入和科技成果产业化产品的销售收入占企业全年总营业收入的50%以上；或者技术性收入占全年总营业收入的20%以上；（七）企业用于产品研究与开发经费占企业全年总营业收入的2%以上。\",\"source\":\"天眼查\",\"orderBy\":28,\"id\":\"1-3SKLS3J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"IPO上市\",\"labelDesc\":\"IPO上市是公司公开募集资本挂牌上市，成为投资者可交易的股份有限公司。在中国内地来说就是通过向上交所，深交所申请，把公司的股票在这两个股票交易所进行交易。\",\"source\":\"天眼查\",\"orderBy\":29,\"id\":\"1-3SKLS67J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"Pre-IPO\",\"labelDesc\":\"对那些已经有上市预期、具备成熟条件的企业进行投资，具有风险小、回收快的特点。通常来说参与这类型的投资都是公司内部的投资者、私募对象或专业的风险基金。\",\"source\":\"天眼查\",\"orderBy\":30,\"id\":\"1-3SKLS60J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"Pre-A轮\",\"labelDesc\":\"公司前期整体数据已经具有一定规模，只是还未占据市场前列，则可以进行PreA轮融资。\",\"source\":\"天眼查\",\"orderBy\":31,\"id\":\"1-3SKLS56J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"Pre-A+轮\",\"labelDesc\":\"在A+轮之前进行的融资\",\"source\":\"天眼查\",\"orderBy\":32,\"id\":\"1-3SKLS54J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"A轮\",\"labelDesc\":\"公司产品有了成熟模样，开始正常运作一段时间并有完整详细的商业及盈利模式，在行业内拥有一定地位和口碑。公司可能依旧处于亏损状态。资金来源一般是专业的风险投资机构（VC）。投资量级在1000万RMB到1亿RMB。\",\"source\":\"天眼查\",\"orderBy\":33,\"id\":\"1-3SKLS35J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"A+轮\",\"labelDesc\":\"就是在A轮融完以后，有新的投资机构想进来，此时公司业务尚未有新的进展，估值也没有 发生变化。 \",\"source\":\"天眼查\",\"orderBy\":34,\"id\":\"1-3SKLS40J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"A++轮\",\"labelDesc\":\"在完成A轮融资后，又进行了两轮融资\",\"source\":\"天眼查\",\"orderBy\":35,\"id\":\"1-3SKLS31J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"Pre-B轮\",\"labelDesc\":\"就是B轮之前的融资。Pre-B轮融资一般指的是介于“天使轮融资”与“B轮融资”之间的融资阶段，一般金额介于“天使轮”与“B轮”之间。\",\"source\":\"天眼查\",\"orderBy\":36,\"id\":\"1-3SKLS25J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"B轮\",\"labelDesc\":\"公司经过一轮烧钱后，获得较大发展。一些公司已经开始盈利。商业模式盈利模式没有任何问题。可能需要推出新业务、拓展新领域。资金来源一般是大多是上一轮的风险投资机构跟投、新的风投机构加入、私募股权投资机构（PE）加入。投资量级在2亿RMB以上。\",\"source\":\"天眼查\",\"orderBy\":37,\"id\":\"1-3SKLS42J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"B++轮\",\"labelDesc\":\"在完成B轮融资后，又进行的两轮融资，但还没有到C轮融资\",\"source\":\"天眼查\",\"orderBy\":38,\"id\":\"1-3SKLS28J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"B+轮\",\"labelDesc\":\"在第一轮融资结束，产品推出以后的进一步发展，为了验证企业的经营模式的各个方面是否合理可行而进行的融资。一般来说，企业进行b＋轮融资证明企业的商业模式已经充分被验证，公司业务正在快速扩张，在这一轮融资里，主要是为了进一步发展而进行的。\",\"source\":\"天眼查\",\"orderBy\":39,\"id\":\"1-3SKLS8J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"Pre-C轮\",\"labelDesc\":\"在C轮融资前做的一轮融资\",\"source\":\"天眼查\",\"orderBy\":40,\"id\":\"1-3SKLS49J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"C轮\",\"labelDesc\":\"公司非常成熟，离上市不远了。应该已经开始盈利，行业内基本前三把交椅。这轮除了拓展新业务，也有补全商业闭环、写好故事准备上市的意图。资金来源主要是PE，有些之前的VC也会选择跟投。投资量级：10亿RMB以上一般C轮后就是上市了。\",\"source\":\"天眼查\",\"orderBy\":41,\"id\":\"1-3SKLS34J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"C+轮\",\"labelDesc\":\"在本轮估值不变情况下的追加融资”，也就是说，如果原本C轮已经出让5%股份获得1亿美元的资本进入，那么假如之后又出让了5%股份又融了1亿美金，那么后面这轮融资就叫C+轮\",\"source\":\"天眼查\",\"orderBy\":42,\"id\":\"1-3SKLS37J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"C++轮\",\"labelDesc\":\"在完成C轮融资后又进行的两轮融资\",\"source\":\"天眼查\",\"orderBy\":43,\"id\":\"1-3SKLS36J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"D轮\",\"labelDesc\":\"D轮是对C轮的升级，企业的发展阶段与C轮的企业相差不大，由于企业为了扩大自身的优势，需要更多的资金投入来提升企业的竞争力，为此企业会进行持续的融资，通过资本优势来提高企业的竞争力。\",\"source\":\"天眼查\",\"orderBy\":44,\"id\":\"1-3SKLS59J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"D+轮\",\"labelDesc\":\"在完成D轮融资后，又进行的一轮融资，此时企业一般都趋于成熟了。\",\"source\":\"天眼查\",\"orderBy\":45,\"id\":\"1-3SKLS12J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"E轮\",\"labelDesc\":\"E轮融资意味着企业仍然未上市,E轮融资一般是烧钱抢占市场,说明投资方看好公司发展前景,公司业务在持续扩张\",\"source\":\"天眼查\",\"orderBy\":46,\"id\":\"1-3SKLS63J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"E+轮\",\"labelDesc\":\"在完成E轮融资后又进行的一轮融资\",\"source\":\"天眼查\",\"orderBy\":46,\"id\":\"1-3SKLS47J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"F轮\",\"labelDesc\":\"公司进行的第六轮融资\",\"source\":\"天眼查\",\"orderBy\":47,\"id\":\"1-3SKLS46J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"天使轮\",\"labelDesc\":\"天使轮公司即拥有“天使投资”的创业公司，公司在创业初期拿到的第一轮外部股权投资一般称为“天使轮投资”。“天使投资”是指个人或机构出资协助具有专门技术或独特概念而缺少自有资金的创业家进行创业，并承担创业中的高风险和享受创业成功后的高收益的一种投资形式。\",\"source\":\"天眼查\",\"orderBy\":48,\"id\":\"1-3SKLS24J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"种子轮\",\"labelDesc\":\"种子融资，是指最早阶段进行的融资方式。虽然大多数初创企业都依靠创始人自己的或其直系亲属和朋友的资金，一些企业还是会寻求第三方的“种子融资”，这是一种最早期阶段进行的融资方式\",\"source\":\"天眼查\",\"orderBy\":49,\"id\":\"1-3SKLS11J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"在业\",\"labelDesc\":\"企业正常开工生产，新建企业包括部分投产或试营业。因不同省份可能有细微的区别，一般在营、正常、经营、在营在册、有效、在业在册也是在业的意思。\",\"source\":\"天眼查\",\"orderBy\":50,\"id\":\"1-3SKLS32J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"存续\",\"labelDesc\":\"公司续存又可以叫做公司存续，是指：企业依法存在并继续正常运营。也被称作开业、正常、登记。因为不同省份的不一样，一般在营、正常、经营、在营在册、有效、在业在册也是在业的意思。\",\"source\":\"天眼查\",\"orderBy\":51,\"id\":\"1-3SKLS48J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"开业\",\"labelDesc\":\"开业是指企业处于正常的营业状态之中，仍有正常的生产经营活动，企业一般都会有营业利润。\",\"source\":\"天眼查\",\"orderBy\":52,\"id\":\"1-3SKLS65J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"正常\",\"labelDesc\":\"企业依法存在并继续正常运营。\",\"source\":\"天眼查\",\"orderBy\":53,\"id\":\"1-3SKLS7J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"仍注册\",\"labelDesc\":\"没有销户，企业短期内还在运营 \",\"source\":\"天眼查\",\"orderBy\":54,\"id\":\"1-3SKLS5J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"迁出\",\"labelDesc\":\"工商网上显示企业迁出是指该企业的注册地址已经变更。并且在工商局办理了企业注册地址变更，所以工商网会现在企业迁出\",\"source\":\"天眼查\",\"orderBy\":55,\"id\":\"1-3SKLS21J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"注销\",\"labelDesc\":\"指一个公司宣告破产，被其它公司收购、规定的营业期限届满不续、或公司内部解散等情形时，公司需要到登记机关申请注销，终止公司法人资格的过程。注销是企业合法退出市场的唯一方式。比如，经过注销登记的公司，法人资格就此终结，员工全部遣散，债权债务关系全面清理完毕，公司至此消失了。\",\"source\":\"天眼查\",\"orderBy\":56,\"id\":\"1-3SKLS39J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"吊销\",\"labelDesc\":\"吊销公司营业执照，吊销企业法人营业执照，是工商行政管理局根据国家工商行政法规对违法的企业法人作出的一种行政处罚。企业法人被吊销营业执照后，应当依法进行清算，清算程序结束并办理工商注销登记后，该企业法人才归于消灭。\",\"source\":\"天眼查\",\"orderBy\":57,\"id\":\"1-3SKLS27J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"废止\",\"labelDesc\":\"相当公司倒闭。废止公司状态，说明这个公司的相关的业务已经停止或者是相关的权限已经是处于没有的状态。相当于倒闭的公司一样，虽然还有一个名字存在，但是他的业务已经归零了，或者是一个破败不堪的样子。\",\"source\":\"天眼查\",\"orderBy\":58,\"id\":\"1-3SKLS38J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"已告解散\",\"labelDesc\":\"是指已成立的公司，因发生法律或章程规定的解散事由而停止营业活动，开始处理未了结的事务，并逐步终止其法人资格的行为。它具有以下特征：　　（一）公司解散的目的和结果是消灭公司法人人格；　　（二）公司解散后后，公司并未立即终止，其法人资格仍然存在，一直到公司清算完毕并注销后才消灭其主体资格；　　（三）公司解散必须经过法定清算程序，但公司因合并、分立而解散时，不必清算。\",\"source\":\"天眼查\",\"orderBy\":59,\"id\":\"1-3SKLS43J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"证书废止\",\"labelDesc\":\"证书废止是一种行政许可的法律行为，其具有剥夺性、不可逆转性以及补救性。\",\"source\":\"天眼查\",\"orderBy\":60,\"id\":\"1-3SKLS51J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"解散\",\"labelDesc\":\"公司解散是指已经成立的公司，因公司章程或者法定事由出现而停止公司的对外经营活动，并开始公司的清算，处理未了结事务从而使公司法人资格消灭的法律行为。根据公司解散是否属于自愿，公司的解散事由可分为两大类，一类是任意解散事由;另一类是强制解散事由。\",\"source\":\"天眼查\",\"orderBy\":61,\"id\":\"1-3SKLS53J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"吊销，已注销\",\"labelDesc\":\"公司营业执照被吊销，该公司的主体资格不存在，经营资格也已经取消。\",\"source\":\"天眼查\",\"orderBy\":62,\"id\":\"1-3SKLS62J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"吊销，未注销\",\"labelDesc\":\"经营状态吊销未注销一般是指，工商局对违法企业作出的吊销企业营业执照的行政处罚。企业被吊销执照后，主体资格依然存在，但经营资格已经取消被依法进行清算。只有清算结束并办理工商注销登记后，这个企业法人才会归于消灭。\",\"source\":\"天眼查\",\"orderBy\":63,\"id\":\"1-3SKLS69J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"核准设立\",\"labelDesc\":\"显示核准设立就证明已经注册成功可以申领营业执照和公章了\",\"source\":\"天眼查\",\"orderBy\":64,\"id\":\"1-3SKLS61J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"中国香港企业\",\"labelDesc\":\"在中国香港地区完成登记注册的企业。\",\"source\":\"天眼查\",\"orderBy\":65,\"id\":\"1-3SKLS68J\",\"created\":\"2022-04-17T23:23:00Z\"},{\"labelName\":\"中国台湾企业\",\"labelDesc\":\"中国台湾地区的企业或个人在大陆地区创建的企业。\",\"source\":\"天眼查\",\"orderBy\":66,\"id\":\"1-3SKLS14J\",\"created\":\"2022-04-17T23:23:00Z\"}]";

    @Test
    public void testCreateInde(){
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(Goods.class);
      //  boolean withMapping = indexOperations.createWithMapping();
        //Map<String,Object> settings = new HashMap<>();
        /*settings.put("shards",2);
        settings.put("replicas",1);
        Document mapping = indexOperations.createMapping();
        Settings settings1 = indexOperations.createSettings();

        boolean b = indexOperations.create(settings, mapping);*/
        indexOperations.createWithMapping();


    }



    @Test
    public void insertDoc(){



    }

    @Test
    public void querys() throws IOException {
        BoolQueryBuilder should = QueryBuilders.boolQuery().should(QueryBuilders.termQuery("goodsName", "小米"));
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(should);
        SearchHits<Goods> search = elasticsearchRestTemplate.search(nativeSearchQuery, Goods.class);
  //     search.stream().forEach(System.out::println);

        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("goodsName", "华为");
        //相关查询
        QueryBuilders.boostingQuery(QueryBuilders.matchQuery("goodsName","华为"),QueryBuilders.matchQuery("goodsName","小米"));
        //范围查询
        QueryBuilders.rangeQuery("price").gt(2000);


        //

        FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery("goodsName", "小2").fuzziness(Fuzziness.fromEdits(1)).boost(0.1F);
        NativeSearchQuery nativeSearchQuery1 = new NativeSearchQuery(fuzzyQueryBuilder);

//前缀查询
        QueryBuilders.prefixQuery("goodsName","华");
//通配符
        QueryBuilders.wildcardQuery("goodsName","华*");
//模糊
        QueryBuilders.fuzzyQuery("goodsName","小");
//id查询
        QueryBuilders.idsQuery().addIds("1001");
//聚合
        MaxAggregationBuilder field = AggregationBuilders.max("max_price").field("price");
        nativeSearchQuery.addAggregation(field);
        //全文 条件查询
        QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery("+lisi-wangw");

        SimpleQueryStringBuilder simpleQueryStringBuilder = QueryBuilders.simpleQueryStringQuery("+lisi-wangw");


        SearchHits<Goods> search1 = elasticsearchRestTemplate.search(nativeSearchQuery1, Goods.class);

        System.out.println(search1);

    }

    @Test
    public void deleteIndex(){
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(Goods.class);
        indexOperations.delete();
    }

}

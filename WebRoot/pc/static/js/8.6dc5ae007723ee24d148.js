webpackJsonp([8],{"Cx/y":function(t,a,s){"use strict";(function(t){var i=s("QQ8u"),n=s("2Uyi");a.a={data:function(){return{imgurl1:"static/images/ic_loan1.png",imgurl2:"static/images/ic_loan2.png",imgurl3:"static/images/ic_loan3.png",imgurl4:"static/images/ic_loan4.png",loanList:[]}},components:{vBreadnav:i.a},methods:{getloanList:function(a){var s=this,i={};i.pageNum=a||1,i.pageSize=10,i.platForm="020003",t.ajax({url:n.a+"/queryFinanceList",type:"get",data:i,dataType:"json",asnyc:!0,success:function(t){t.code==n.b&&(console.log(t.data),s.loanList=t.data.financeList)}})},toLoanForm:function(t){this.$router.push({name:"loanForm",params:{id:t}})}},mounted:function(){this.getloanList()}}}).call(a,s("slT5"))},Tlyu:function(t,a,s){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var i=s("Cx/y"),n=function(){var t=this,a=t.$createElement,s=t._self._c||a;return s("div",{staticClass:"container"},[s("div",{staticClass:"row"},[s("div",{staticClass:"col-xs-12"},[s("v-breadnav",[t._v("惠鑫贷")])],1)]),t._v(" "),s("div",{staticClass:"row loan"},[s("div",{staticClass:"col-xs-12"},[0!=t.loanList.length?s("ul",{staticClass:"loan-list"},t._l(t.loanList,function(a,i){return s("li",{key:i,staticClass:"loan-item clearfix"},[s("div",{staticClass:"col-xs-3"},[s("div",{staticClass:"loan-item-img item1"},[s("h3",[t._v(t._s(a.financeName))]),t._v(" "),s("div",{staticClass:"icon"},[s("img",{staticClass:"img-responsive",attrs:{src:a.icon,alt:""}})]),t._v(" "),s("button",{staticClass:"btn",attrs:{"data-loanid":a.id},on:{click:function(s){t.toLoanForm(a.id)}}},[t._v("立即申请")])])]),t._v(" "),s("div",{staticClass:"col-xs-9"},[s("table",{staticClass:"loan-text"},[s("tbody",[t._m(0,!0),t._v(" "),s("tr",[s("td",[s("span",{staticClass:"red"},[t._v(t._s(a.rate))])]),t._v(" "),s("td",[s("span",{staticClass:"red"},[t._v(t._s(a.moneys))])])]),t._v(" "),t._m(1,!0),t._v(" "),s("tr",[s("td",[s("span",{staticClass:"red"},[t._v(t._s(a.period))])]),t._v(" "),s("td",[s("span",{staticClass:"text"},[t._v(t._s(a.standard))])])])])])])])})):t._e()])])])},e=[function(){var t=this,a=t.$createElement,s=t._self._c||a;return s("tr",[s("td",[t._v("最低首付比例")]),t._v(" "),s("td",[t._v("最低贷款额")])])},function(){var t=this,a=t.$createElement,s=t._self._c||a;return s("tr",[s("td",[t._v("贷款期限")]),t._v(" "),s("td",[t._v("申请提交资料及二手车准入标准")])])}],l={render:n,staticRenderFns:e},c=l,o=s("vSla"),r=o(i.a,c,!1,null,null,null);a.default=r.exports}});
//# sourceMappingURL=8.6dc5ae007723ee24d148.js.map
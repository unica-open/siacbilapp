<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<script type="text/javascript" src="${jspathexternal}${jquery.version}.js"></script>
<%-- Caricamento del framework Bootstrap per JavaScript --%>
<%-- <script type="text/javascript" src="${jspathexternal}bootstrap.js"></script> --%>
<script type="text/javascript" src="${jspathexternal}bootstrap-transition.js"></script>
<script type="text/javascript" src="${jspathexternal}bootstrap-alert.js"></script>
<script type="text/javascript" src="${jspathexternal}bootstrap-button.js"></script>
<script type="text/javascript" src="${jspathexternal}bootstrap-carousel.js"></script>
<script type="text/javascript" src="${jspathexternal}bootstrap-collapse.js"></script>
<script type="text/javascript" src="${jspathexternal}bootstrap-dropdown.js"></script>
<!-- SIAC-7015 -->
<script type="text/javascript" src="${jspathexternal}bootstrap-modal.fork.js"></script>

<script type="text/javascript" src="${jspathexternal}bootstrap-tooltip.js"></script>
<script type="text/javascript" src="${jspathexternal}bootstrap-popover.js"></script>
<script type="text/javascript" src="${jspathexternal}bootstrap-scrollspy.js"></script>
<script type="text/javascript" src="${jspathexternal}bootstrap-tab.js"></script>
<%-- <script type="text/javascript" src="${jspathexternal}bootstrap-typeahead.js"></script> --%>
<script type="text/javascript" src="${jspathexternal}bootstrap-affix.js"></script>
<script type="text/javascript" src="${jspathexternal}bootstrap-select.js"></script>
<script type="text/javascript" src="${jspathexternal}bootbox-3.3.0.js"></script>
<%-- Caricamento del framework Data Table per JavaScript --%>
<script type="text/javascript" src="${jspathexternal}dataTable/jquery.dataTables.fork${datatables.version}.js"></script> 
<script type="text/javascript" src="${jspathexternal}dataTable/bootstrap.dataTables.min.js"></script>
<script type="text/javascript" src="${jspathexternal}dataTable/dataTables-ajaxRedraw.js"></script>
<script type="text/javascript" src="${jspathexternal}dataTable/bootstrap.dataTables.firstlast.js"></script>
<%-- Caricamento del framework Google Code Prettify per JavaScript --%>
<script type="text/javascript" src="${jspathexternal}google-code-prettify/prettify.js"></script>
<%-- Caricamento del framework Wizard per JavaScript --%>
<script type="text/javascript" src="${jspathexternal}bwizard.min.js"></script>
<%-- Caricamento del framework ZTree per JavaScript --%>
<script type="text/javascript" src="${jspathexternal}tree/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${jspathexternal}tree/jquery.ztree.excheck-3.5.js"></script>
<%-- Caricamento del calendario Datepicker for Bootstrap --%>
<script type="text/javascript" src="${jspathexternal}bootstrap-datepicker.js"></script>
<script type="text/javascript" src="${jspathexternal}bootstrap-datepicker.it.js"></script>
<%-- Caricamento del Chosen per jQuery --%>
<script type="text/javascript" src="${jspathexternal}chosen/chosen.jquery.js"></script>
<script type="text/javascript" src="${jspathexternal}chosen/prism.js" charset="utf-8"></script>
<%-- Caricamento jQuery Multiselect --%>
<script type="text/javascript" src="${jspathexternal}jquery.multi-select.js"></script>
<%-- Bignumber.js (gestione decimali) --%>
<script type="text/javascript" src="${jspathexternal}bignumber/bignumber${datatables.version}.js"></script>
<%-- Caricamento delle librerie custom --%>
<script type="text/javascript" src="${jspath}jquery.bs.overlay.js"></script>
<script type="text/javascript" src="${jspath}jquery.bs.allowedChars.js"></script>

<%-- File di funzioni JavaScript comuni --%>
<script type="text/javascript" src="${jspath}polyfill.js"></script>
<script type="text/javascript" src="${jspath}common.js"></script>
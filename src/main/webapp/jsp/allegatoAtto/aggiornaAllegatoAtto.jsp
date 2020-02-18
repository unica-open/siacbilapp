<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:include value="/jsp/include/messaggi.jsp" />
				<h3><s:property value="denominazioneAllegatoAtto"/><span class="pull-right">Versione <s:property value="allegatoAtto.versioneInvioFirmaNotNull"/></span></h3>
				<input type="hidden" id="statoOperativoAllegatoAtto" value="<s:property value="allegatoAtto.statoOperativoAllegatoAtto.codice" />" />
				<ul class="nav nav-tabs">
					<li <s:if test='%{"DATI".equals(tabVisualizzazione.name())}'>class="active"</s:if>>
						<a data-toggle="tab" href="#tabDatiAllegato" id="navDatiAllegato">Dati atto</a>
					</li>
					<li <s:if test='%{"ELENCO".equals(tabVisualizzazione.name())}'>class="active"</s:if>>
						<a data-toggle="tab" href="#tabElenchiCollegati" id="navElenchiCollegati">Elenchi collegati</a>
					</li>
					<li <s:if test='%{"DURC".equals(tabVisualizzazione.name())}'>class="active"</s:if>>
						<a data-toggle="tab" href="#tabDurc" id="navDurc">DURC</a>
					</li>
				</ul>
				<div class="tab-content noOverflow">
					<div id="tabDatiAllegato" class="tab-pane fade<s:if test='%{"DATI".equals(tabVisualizzazione.name())}'> in active</s:if>">
						<s:include value="/jsp/allegatoAtto/aggiornamento/datiAllegato.jsp" />
					</div>
					<div id="tabElenchiCollegati" class="tab-pane fade<s:if test='%{"ELENCO".equals(tabVisualizzazione.name())}'> in active</s:if>">
						<s:include value="/jsp/allegatoAtto/aggiornamento/elenchiCollegati.jsp" />
					</div>
					<div id="tabDurc" class="tab-pane fade<s:if test='%{"DURC".equals(tabVisualizzazione.name())}'> in active</s:if>">
						<s:include value="/jsp/allegatoAtto/aggiornamento/durc.jsp" />
					</div>
				</div>
			</div>
		</div>
	</div>

	<s:include value="/jsp/allegatoAtto/aggiornamento/modaleSospensioneSoggettoElenco.jsp" />
	<s:include value="/jsp/include/modaleConfermaProsecuzioneCambioTab.jsp" />
	<s:include value="/jsp/include/modaleConfermaEliminazione.jsp"/>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}provvisorioDiCassa/ricerca.js"></script>
	<script type="text/javascript" src="${jspath}allegatoAtto/gestioneElenco.js"></script>
	<script type="text/javascript" src="${jspath}codiceFiscale.js"></script>
	<script type="text/javascript" src="${jspath}soggetto/ricerca.js"></script>
	<script type="text/javascript" src="${jspath}allegatoAtto/aggiorna_datiAllegato.js"></script>
	<script type="text/javascript" src="${jspath}allegatoAtto/aggiorna_elenchiCollegati.js"></script>
	<script type="text/javascript" src="${jspath}allegatoAtto/aggiorna_durc.js"></script>
	<script type="text/javascript" src="${jspath}documento/ztree.js"></script>
</body>
</html>
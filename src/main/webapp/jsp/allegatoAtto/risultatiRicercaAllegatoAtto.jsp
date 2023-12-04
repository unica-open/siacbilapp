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
				<s:form action="#" method="post" id="formRisultatiRicercaAllegatoAtto">
					<s:include value="/jsp/include/messaggi.jsp" />
					<s:hidden name="savedDisplayStart" id="HIDDEN_savedDisplayStart" />
					<h3>Risultati di ricerca atto contabile / allegato</h3>
					<h4 class="step-pane">Dati di ricerca: <s:property value="stringaRiepilogo"/></h4>
					<fieldset class="form-horizontal">
						<h4><span id="id_num_result" class="num_result"></span></h4>
						<table class="table table-hover tab_left dataTable" id="tabellaRisultatiRicercaAllegatoAtto">
							<thead>
								<tr>
									<th>Provvedimento</th>
									<th></th>
									<th>Sospensione</th>
									<th>Causale</th>
									<th>Data scadenza</th>
									<th>Rich.DURC</th>
									<th>Data fine validita DURC</th>
									<th>Ordinativi</th>
									<th>Stato</th>
									<th class="tab_Right"></th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</fieldset>

					<s:include value="/jsp/include/modaleAnnullamentoRisultatiRicerca.jsp">
						<s:param name="href">risultatiRicercaAllegatoAtto_annulla.do</s:param>
					</s:include>
					<s:include value="/jsp/allegatoAtto/modaleCompletamentoRisultatiRicerca.jsp" />
					<s:if test="abilitaChecklist()">
						<s:include value="/jsp/allegatoAtto/modaleInvioConChecklistRisultatiRicerca.jsp" />
					</s:if>
					<s:else>					
						<s:include value="/jsp/allegatoAtto/modaleInvioRisultatiRicerca.jsp" />
					</s:else>

					<div class="Border_line"></div>
					<p>
						<s:include value="/jsp/include/indietro.jsp" />
					</p>
					<input type="hidden" name="uidAllegatoAtto" id="hiddenUidAllegatoAtto"/>
				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/allegatoAtto/risultatiRicerca.js"></script>

</body>
</html>
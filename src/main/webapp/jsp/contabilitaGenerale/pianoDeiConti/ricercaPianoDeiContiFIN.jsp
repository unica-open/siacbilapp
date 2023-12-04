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
				<s:form cssClass="form-horizontal" action="ricercaPianoDeiContiFIN" id="formRicercaPianoDeiConti">
					<s:include value="/jsp/include/messaggi.jsp" />
					
					<h3>Ricerca Piano dei Conti</h3>

					<div class="step-content">
						<div class="step-pane active" id="step1">
						
							<fieldset class="form-horizontal">
								<fieldset id="fieldsetRicerca">
									<h4 class="step-pane">Dati Piano dei Conti</h4>
									
									
									<div class="control-group">
										<label class="control-label" for="classePianoDeiContiRicerca">Classe *</label>
										<div class="controls">
											<s:select list="listaClassi" name="conto.pianoDeiConti.classePiano.uid"
												id="classePianoDeiContiRicerca" cssClass="span6" headerKey="0" headerValue=""
												listKey="uid" listValue="%{codice + ' - ' + descrizione}" />
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label" for="codificaInternaPianoDeiContiRicerca">Codifica interna</label>
										<div class="controls">
											<s:textfield id="codificaInternaPianoDeiContiRicerca" name="conto.codiceInterno" cssClass="span2" />
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label" for="codicePianoDeiContiRicerca">Codice conto *</label>
										<div class="controls">
											<s:textfield id="codicePianoDeiContiRicerca" name="conto.codice" cssClass="span6" />
											<span id="descrizionePianoDeiContiRicerca"></span>
											<%--span class="radio guidata"><a class="btn btn-primary">compilazione guidata</a></span--%>
											<button type="button" class="btn btn-primary pull-right" id="pulsanteCompilazioneGuidataConto">compilazione guidata</button>	
										</div>
									</div>
								
								
									<div class="Border_line"></div>
									<p>
										<button type="button" class="btn btn-primary pull-right" id="pulsanteRicercaPianoDeiConti">
													cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteRicercaPianoDeiConti"></i>
										</button>
									</p>				
									<div class="clear"></div> 
									
									
								</fieldset>
							</fieldset>
							
							<s:hidden id="HIDDEN_tabellaVisibile" name="isTabellaVisibile"/>
							<div id = "risultatiRicercaPianoDeiConti" class="hide">
									<s:include value="/jsp/contabilitaGenerale/pianoDeiConti/risultatiRicercaPianoDeiContiFIN.jsp" />
							</div>
							
						<s:include value="/jsp/include/indietro.jsp" />	
						</div>
						
					</div>				
				</s:form>
				<s:include value="/jsp/contabilitaGenerale/include/modaleRicercaConto.jsp" />
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/contabilitaGenerale/ricercaConto.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/contabilitaGenerale/pianoDeiConti/risultatiRicercaPianoDeiContiFIN.js"></script>
</body>
</html>
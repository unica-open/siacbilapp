<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form cssClass="form-horizontal" method="post" action="cassaEconomaleStampe_enterStep2" novalidate="novalidate" id="formStampaRendicontoCassa">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Stampa Rendiconto</h3>
					<fieldset class="form-horizontal">
						<br />
						<div class="step-content">
							<div class="step-pane active" id="step1">

								<h4 class="step-pane">Dati</h4>
								<div class="control-group">
									<label for="cassaEconomale" class="control-label">Cassa economale*</label>
									<div class="controls">
										<s:select list="listaCasseEconomali" cssClass="span6" required="true" name="cassaEconomale.uid" id="cassaEconomale"
											headerKey="" headerValue="Selezionare la cassa economale" listKey="uid" listValue="%{codice + ' - ' + descrizione}"
											disabled="%{unicaCassa}" />
									</div>
									<s:if test="%{unicaCassa}">
										<s:hidden name="cassaEconomale.uid" />
									</s:if>
								</div>
								<div class="control-group">
									<label class="control-label">Ultimo numero rendiconto</label>
									<div class="controls">
										<s:textfield id="ultimoNumeroRendiconto" name="stampaRendiconto.numeroRendiconto" cssClass="span2" type="text" disabled="true" />
										<s:hidden name="stampaRendiconto.numeroRendiconto" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Periodo ultimo rendiconto</label>
									<div class="controls">
										<span class="al">
											<label class="radio inline">Da</label>
										</span>
										<s:textfield id="periodoDataInizioRendiconto" name="stampaRendiconto.periodoDataInizio" cssClass="span2" type="text" disabled="true" />
										<span class="al">
											<label class="radio inline">A</label>
										</span>
										<s:textfield id="periodoDataFineRendiconto" name="stampaRendiconto.periodoDataFine" cssClass="span2" type="text" disabled="true" />
										<s:hidden name="stampaRendiconto.periodoDataFine" />
										<s:hidden name="stampaRendiconto.periodoDataInizio" />
									</div>
								</div>

								<div class="control-group">
									<label class="control-label">Tipo stampa</label>
									<div class="controls">
										<s:select list="listaTipoStampa" cssClass="span2" name="tipoStampa" id="tipoStampa" headerKey="" headerValue="" listValue="descrizione" />
									</div>
								</div>

								<div class="control-group">
									<label class="control-label">Periodo da rendicontare *</label>
									<div class="controls">
										<span class="al">
											<label class="radio inline">Da</label>
										</span>
										<s:textfield id="periodoDaRendicontareDataInizio" name="periodoDaRendicontareDataInizio" cssClass="span2 datepicker" type="text" required="true" />
										<span class="al">
											<label class="radio inline">A</label>
										</span>
										<s:textfield id="periodoDaRendicontareDataFine" name="periodoDaRendicontareDataFine" cssClass="span2 datepicker" type="text" required="true" />
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label">Data rendiconto</label>
									<div class="controls">
										<s:textfield id="dataRendiconto" name="stampaRendiconto.dataRendiconto" cssClass="span2 datepicker" type="text" />
									</div>
								</div>

								<div class="control-group">
									<label class="control-label">Numero rendiconto</label>
									<div class="controls">
										<s:textfield id="numeroRendicontoDisabled" name="numeroRendiconto" cssClass="span2" type="text" disabled="true" />
										<s:hidden name="numeroRendiconto" />
									</div>
								</div>

							</div>
						</div>

					</fieldset>
					<p class="margin-large">
						<s:include value="/jsp/include/indietro.jsp" />
						<button type="submit" class="btn btn-primary pull-right">prosegui</button>
					</p>
				</s:form>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/cassaEconomale/stampe/stampaCECRendiconto.step1.js"></script>

</body>
</html>
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
				<s:form action="effettuaRicercaStampeCassaEconomale" novalidate="novalidate" method="post" cssClass="form-horizontal">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Ricerca stampe</h3>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<h4>Dati principali</h4>
							<fieldset class="form-horizontal">
								<div class="control-group">
									<label for="cassaEconomale" class="control-label">Cassa economale*</label>
									<div class="controls">
										<s:select list="listaCasseEconomali" cssClass="span6" required="true" name="cassaEconomale.uid" id="cassaEconomale"
											headerKey="" headerValue="Selezionare la cassa economale" listKey="uid" listValue="%{codice + ' - ' + descrizione}" 
											disabled="%{unicaCassa}" data-maintain="" />
									</div>
									<s:if test="%{unicaCassa}">
										<s:hidden name = "cassaEconomale.uid" data-maintain=""></s:hidden>
									</s:if>
								</div>
								
								<div class="control-group">
									<label for="tipoStampa" class="control-label">Tipo stampa*</label>
									<div class="controls">
										<s:select list="listaTipoDocumento" cssClass="span6" required="true" name="stampeCassaFile.tipoDocumento" id="tipoStampa"
											headerKey="" headerValue="Selezionare il tipo di stampa" listValue="descrizione" />
									</div>
								</div>
								
								<div id="campiStampaCassa" class="hide">
									
									<div class="control-group" data-rendiconto="" data-giornale="">
										<label for="statoStampa" class="control-label">Tipo</label>
										<div class="controls">
											<s:select list="listaTipoStampa" id="statoStampa" name="stampeCassaFile.tipoStampa" cssClass="span6" headerKey="" headerValue=""
												listValue="descrizione" />
										</div>
									</div>
									
									<div class="control-group" data-giornale="">
										<label for="dataGiornale" class="control-label">Data giornale</label>
										<div class="controls">
											<span>Da <s:textfield id="dataGiornaleUltimaStampaDa" name="dataGiornaleUltimaStampaDa" cssClass="span2 datepicker" /></span>
											<span>A <s:textfield id="dataGiornaleUltimaStampaA" name="dataGiornaleUltimaStampaA" cssClass="span2 datepicker" /></span>
										</div>
									</div>
									
									<div class="control-group" data-rendiconto="">
										<label  class="control-label">Periodo rendicontato</label>
										<div class="controls">
											<span>Da <s:textfield id="periodoRendicontazioneDa" name="stampeCassaFile.stampaRendiconto.periodoDataInizio" cssClass="span2 datepicker" /></span>
											&nbsp;
											<span>A <s:textfield id="periodoRendicontazioneA" name="stampeCassaFile.stampaRendiconto.periodoDataFine" cssClass="span2 datepicker" /> </span>
										</div>
									</div>
									
									<div class="control-group" data-rendiconto="">
										<label for="dataRendicontazione" class="control-label">Data Rendicontazione</label>
										<div class="controls">
											<s:textfield id="dataRendicontazione" name="stampeCassaFile.stampaRendiconto.dataRendiconto" cssClass="span2 datepicker" />
										</div>
									</div>
									
									<div class="control-group" data-rendiconto="">
										<label for="numeroRendiconto" class="control-label">Numero Rendiconto</label>
										<div class="controls">
											<s:textfield id="numeroRendiconto" name="stampeCassaFile.stampaRendiconto.numeroRendiconto" cssClass="span6" maxlength="10" data-min="1" data-max="%{maxValueForIntegerField}" />
										</div>
									</div>
									
									<div class="control-group" data-ricevuta="">
										<label for="numeroMovimento" class="control-label">Numero Movimento</label>
										<div class="controls">
											<s:textfield id="numeroMovimento" name="stampeCassaFile.numeroMovimento" cssClass="span3" data-min="1" data-max="%{maxValueForIntegerField}" />
										</div>
									</div>
									
									<div class="control-group" data-rendiconto="" data-giornale="" data-ricevuta="">
										<label for="dataCreazione" class="control-label">Data caricamento</label>
										<div class="controls">
											<s:textfield id="dataCreazione" name="stampeCassaFile.dataCreazione" cssClass="span2 datepicker" />
										</div>
									</div>
									
								</div>
							</fieldset>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<s:reset cssClass="btn btn-secondary" value="annulla" />
						<button type="submit" class="btn btn-primary pull-right" id="pulsanteRicercaStampe">cerca</button>
					</p>
				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}cassaEconomale/ricercaStampe.js"></script>

</body>
</html>
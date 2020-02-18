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
				<s:form cssClass="form-horizontal" method="post"
					action="%{urlStampaGiornaleDiCassa}" novalidate="novalidate"
					id="formStampaGiornaleDicassa">
					<s:include value="/jsp/include/messaggi.jsp" />

					<h3>Stampa Giornale di cassa</h3>

					<fieldset class="form-horizontal">
						<br>
						<div class="step-content">
							<div class="step-pane active" id="step1">

								<h4 class="step-pane">Dati</h4>

								<div class="control-group">
									<div class="control-group">
										<label for="cassaEconomale" class="control-label">Cassa
											economale*</label>
										<div class="controls">
											<s:select list="listaCasseEconomali" cssClass="span6"
												required="true" name="cassaEconomale.uid"
												id="cassaEconomale" headerKey=""
												headerValue="Selezionare la cassa economale" listKey="uid"
												listValue="%{codice + ' - ' + descrizione}"
												disabled="%{unicaCassa || flagStampaEffettuata}" />
										</div>
										<s:if test="%{unicaCassa}">
											<s:hidden name="cassaEconomale.uid" />
										</s:if>
									</div>
									<label class="control-label">Data</label>
									<div class="controls">
										<s:textfield id="dataSistema" name="dataSistema"
											cssClass="span2" type="text" disabled="true" />
										<s:hidden name="dataSistema" />
									</div>
								</div>

								<div class="control-group">
									<label class="control-label">Anno di esercizio</label>
									<div class="controls">
										<s:textfield id="annoEsercizio" name="annoEsercizio"
											cssClass="span2" type="text" disabled="true" />
									</div>
								</div>

								<div class="control-group">
									<label class="control-label">Data ultima stampa
										definitiva del giornale</label>
									<div class="controls">
										<s:if test="%{stampaGiornale.dataUltimaStampa==null}">
											<s:textfield id="dataUltimaStampa"
												name="stampaGiornale.dataUltimaStampa" value=""
												cssClass="span2" type="text" disabled="true" />
										</s:if>
										<s:else>
										<s:textfield id="dataUltimaStampa"
												name="stampaGiornale.dataUltimaStampa" cssClass="span2"
												type="text" disabled="true" />
										<s:hidden name="stampaGiornale.dataUltimaStampa" />
												
										</s:else>
										
									</div>
								</div>



								<div class="control-group">
									<label class="control-label">Tipo stampa</label>
									<div class="controls">
									    <s:if test="%{flagStampaEffettuata}">
										<s:select list="listaTipoStampa" cssClass="span2"
											name="tipoStampa" id="tipoStampa" headerKey="" headerValue=""
											listValue="%{descrizione}"  disabled="true" />
										</s:if>	
										<s:else>
										<s:select list="listaTipoStampa" cssClass="span2"
											name="tipoStampa" id="tipoStampa" headerKey="" headerValue="" 
											listValue="%{descrizione}"  />
											<s:hidden name="tipoStampa" />
											
										</s:else>
										
									</div>
								</div>


								<div class="control-group">
									<label class="control-label">Data da elaborare</label>
									<div class="controls">
									<s:if test="%{flagStampaEffettuata}">
									
										<s:textfield id="dataDaElaborare" name="dataDaElaborare"
											cssClass="span2 datepicker" type="text" disabled="true"  />
									</s:if>
									<s:else>
									
										<s:textfield id="dataDaElaborare" name="dataDaElaborare"
											cssClass="span2 datepicker" type="text" />
										<s:hidden name="dataDaElaborare" />	
									</s:else>
									
									
									</div>
								</div>

							</div>
						</div>


					</fieldset>

				    <p class="margin-large">
						<s:include value="/jsp/include/indietro.jsp" />
						
					 <s:if test="%{!flagStampaEffettuata}">
						<button type="button" class="btn btn-primary pull-right"
							id="pulsanteStampa">stampa </button>
                    </s:if>	
					</p>
                    
				</s:form>

			</div>

		</div>
	</div>
	<!-- modale2 per confermare la richiesta di stampa  giornale di cassa dopo il controllo-->
	<div aria-hidden="true"
		aria-labelledby="bodyConfermaStampaGiornaleCassa2" role="dialog"
		tabindex="-1" class="modal hide fade"
		id="modaleConfermaStampaGiornaleCassa2">
		<div class="modal-body" id="bodyConfermaStampaGiornaleCassa2">
			<div class="alert alert-warning alert-persistent">
				<p>
					<strong>Conferma Stampa Giornale di Cassa</strong>
				</p>
				<p >
					Ultima stampa definitiva effettuata in data	<span id="msg2"><s:property value="stampaGiornale.dataUltimaStampa" /></span>&nbsp;, vuoi proseguire?
				</p>
			</div>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn" aria-hidden="true"
				data-dismiss="modal">no, indietro</button>
			<button type="button" class="btn btn-primary"
				id="pulsanteConfermaStampaGiornaleDiCassa2">s&iacute;,
				prosegui</button>
		</div>
	</div>

	<!-- modale1 per confermare la richiesta di stampa  giornale di cassa prima del controllo-->
	<div aria-hidden="true"
		aria-labelledby="bodyConfermaStampaGiornaleCassa1" role="dialog"
		tabindex="-1" class="modal hide fade"
		id="modaleConfermaStampaGiornaleCassa1">
		<div class="modal-body" id="bodyConfermaStampaGiornaleCassa1">
			<div class="alert alert-warning alert-persistent">
				<p>
					<strong>Conferma Stampa Giornale di cassa</strong>
				</p>
				<p>Si sta per elaborare la stampa del Giornale di cassa, vuoi
					proseguire?</p>
			</div>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn" aria-hidden="true"
				data-dismiss="modal">no, indietro</button>
			<button type="button" class="btn btn-primary"
				id="pulsanteConfermaStampaGiornaleDiCassa1">s&iacute;,
				prosegui</button>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}cassaEconomale/stampe/stampaCECGiornaleDiCassa.js"></script>

</body>
</html>
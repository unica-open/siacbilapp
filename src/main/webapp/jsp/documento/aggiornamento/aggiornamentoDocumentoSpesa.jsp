<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<s:hidden name="nomeAzioneSAC" id="nomeAzioneSAC" />
<s:hidden name="datiFatturaPagataIncassataEditabili" id="datiFatturaPagataIncassataEditabili" />
<s:form id="formAggiornamentoDatiDocumento" cssClass="form-horizontal" novalidate="novalidate" action="aggiornamentoDocumentoSpesa_aggiornamentoAnagrafica">
	<s:hidden name="documento.fatturaFEL.idFattura" />
	<h4 class="step-pane">Dati principali</h4>
	<div class="control-group">
		<label for="siopeDocumentoTipo" class="control-label">Tipo documento siope *</label>
		<div class="controls">
			<select name="documento.siopeDocumentoTipo.uid" class="span6" required id="siopeDocumentoTipo" disabled>
				<option value="0"></option>
				<s:iterator value="listaSiopeDocumentoTipo" var="sdt">
					<option value="<s:property value="#sdt.uid" />"
							data-codice="<s:property value="#sdt.codice" />"
							<s:if test="#sdt.uid == documento.siopeDocumentoTipo.uid">selected</s:if>>
						<s:property value="#sdt.codice" /> - <s:property value="#sdt.descrizione" />
					</option>
				</s:iterator>
			</select>
			<s:hidden name="documento.siopeDocumentoTipo.uid" />
		</div>
	</div>
	<div class="control-group <s:if test="!tipoDocumentoSiopeAnalogico">hide</s:if>" data-siope-analogico>
		<label for="siopeDocumentoTipoAnalogico" class="control-label">Tipo documento analogico siope *</label>
		<div class="controls">
			<s:select list="listaSiopeDocumentoTipoAnalogico" cssClass="span6" id="siopeDocumentoTipoAnalogico"
					name="documento.siopeDocumentoTipoAnalogico.uid" headerKey="0" headerValue=""
					listKey="uid" listValue="%{codice + ' - ' + descrizione}" required="true" />
		</div>
	</div>
	<div class="control-group">
		<label for="collegatoCEC" class="control-label">Collegato a Cassa Economale</label>
		<div class="controls">
			<s:checkbox id="collegatoCEC" name="documento.collegatoCEC" disabled="%{!collegatoCECEditabile}"/>
			<s:if test="%{!collegatoCECEditabile}">
				<s:hidden name="documento.collegatoCEC" id="HIDDEN_collegatoCEC" />
			</s:if>
		</div>
	</div>
	<div class="control-group">
		<label for="dataEmissioneDocumento" class="control-label">Data emissione *</label>
		<div class="controls">
			<s:if test="%{documento.fatturaFEL != null}">
				<s:textfield id="dataEmissioneDocumento" name="documento.dataEmissione" cssClass="lbTextSmall span2" size="10" required="required" readonly="true"/>
			</s:if>
			<s:else>
				<s:textfield id="dataEmissioneDocumento" name="documento.dataEmissione" cssClass="lbTextSmall span2 datepicker" size="10" required="required"/>
			</s:else>
			<span class="alRight">
				<label for="dataRicezionePortaleDocumento" class="radio inline">Data ricezione</label>
			</span>
			<s:if test="%{documento.fatturaFEL != null}">
				<s:textfield id="dataRicezioneDocumento" name="documento.dataRicezionePortale" cssClass="lbTextSmall span2" size="10" readonly="true" />
			</s:if>
			<s:else>
				<s:textfield id="dataRicezioneDocumento" name="documento.dataRicezionePortale" cssClass="lbTextSmall span2 datepicker" size="10"/>
			</s:else>
		</div>
	</div>
	<h4 class="step-pane">Dati importi</h4>
	<div class="control-group">
		<label class="control-label" for="importoDocumento">Importo *</label>
		<div class="controls">
			<s:textfield id="importoDocumento" cssClass="lbTextSmall span2 soloNumeri decimale" name="documento.importo" placeholder="importo" required="required" disabled="%{documentoRegolarizzazioneFiglioDiEntrata}" />
			<span class="alRight">
				<label class="radio inline" for="arrotondamentoDocumento">Arrotondamento</label>
			</span>
			<s:textfield id="arrotondamentoDocumento" cssClass="lbTextSmall span2 soloNumeri decimale" name="documento.arrotondamento" placeholder="arrotondamento" disabled="%{documentoRegolarizzazioneFiglioDiEntrata}" />
			<span class="alRight">
				<label class="radio inline" for="nettoDocumento">Netto</label>
			</span>
			<s:textfield id="nettoDocumento" readonly="true" cssClass="lbTextSmall span2 soloNumeri decimale" name="netto" />
			<s:hidden name="oldArrotondamento" id="HIDDEN_oldArrotondamento" />
			<s:hidden name="oldNetto" id="HIDDEN_oldNetto" />
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label" for="noteCreditoDocumento">Note credito</label>
		<div class="controls">
			<s:textfield id="noteCreditoDocumento" name="totaleNoteCredito" cssClass="lbTextSmall span2" disabled="true" />
			<span class="alRight">
				<label for="penaleAltroDocumento" class="radio inline">Penale/Altro</label>
			</span>
			<s:textfield id="penaleAltroDocumento" name="penaleAltroDocumento" cssClass="lbTextSmall span2" disabled="true" />
		</div>
	</div>

	<div class="control-group">
		<label for="descrizioneDocumento" class="control-label">Descrizione *</label>
		<div class="controls">
			<s:textarea id="descrizioneDocumento" name="documento.descrizione" cols="15" rows="2" cssClass="span10" required="required"></s:textarea>
		</div>
	</div>
	<%-- jira 1905 visualizzo il soggetto con campi non editabili se il documento e' ! incompleto --%>
	<s:if test="documentoIncompleto">
		<h4 class="step-pane">Soggetto
			<span id="descrizioneCompletaSoggetto">
				<s:if test='%{soggetto != null && (soggetto.codice ne null && soggetto.codice != "") && (soggetto.descrizione ne null && soggetto.descrizione != "") && (soggetto.codiceFiscale ne null && soggetto.codiceFiscale != "")}'>
					<s:property value="%{soggetto.codice + ' - ' + soggetto.descrizione + ' - ' + soggetto.codiceFiscale}" />
				</s:if>
			</span>
		</h4>
		<s:hidden id="HIDDEN_soggettoDenominazione" name="soggetto.denominazione" />
		<s:hidden id="HIDDEN_soggettoCodiceFiscale" name="soggetto.codiceFiscale" />
		<div class="control-group">
			<label class="control-label" for="codiceSoggetto">Codice *</label>
			<div class="controls">
				<s:textfield id="codiceSoggetto" cssClass="lbTextSmall span2" name="soggetto.codiceSoggetto" maxlength="20" placeholder="codice" required="required" />
				<span class="radio guidata">
					<a href="#" class="btn btn-primary" id="pulsanteAperturaCompilazioneGuidataSoggetto">compilazione guidata</a>
				</span>
			</div>
		</div>
		<s:if test="fatturaFELPresente">
			<div class="control-group">
				<label class="control-label" for="identificativoFiscaleFEL">Identificatovo fiscale FEL</label>
				<div class="controls">
					<s:textfield id="identificativoFiscaleFEL" value="%{identificativoFiscaleFEL}" cssClass="lbTextSmall span2" readonly="true" />
					<span class="alRight">
						<label for="codiceFiscaleFEL" class="radio inline">Codice fiscale FEL</label>
					</span>
					<s:textfield id="codiceFiscaleFEL" value="%{codiceFiscaleFEL}" cssClass="lbTextSmall span2" maxlength="20" readonly="true"/>
				</div>
			</div>
		</s:if>
		<div class="control-group">
			<label class="control-label">Struttura Amministrativa</label>
			<div class="controls">
				<div class="accordion span8 struttAmm">
					<div class="accordion-group">
						<div class="accordion-heading">
							<a class="accordion-toggle" id="accordionPadreStrutturaAmministrativa_Doc" href="#struttAmm_Doc">
								<span id="SPAN_StrutturaAmministrativoContabile_Doc">Seleziona la Struttura amministrativa</span>
							</a>
						</div>
						<div id="struttAmm_Doc" class="accordion-body collapse">
							<div class="accordion-inner">
								<ul id="treeStruttAmm_Doc" class="ztree treeStruttAmm"></ul>
							</div>
						</div>
					</div>
				</div>
				<s:hidden id="HIDDEN_StrutturaAmministrativoContabile_DocUid" name="documento.strutturaAmministrativoContabile.uid" />
				<s:hidden id="HIDDEN_StrutturaAmministrativoContabile_DocCodice" name="documento.strutturaAmministrativoContabile.codice" />
				<s:hidden id="HIDDEN_StrutturaAmministrativoContabile_DocDescrizione" name="documento.strutturaAmministrativoContabile.descrizione" />
			</div>
		</div>
		<div class="control-group">
			<label for="beneficiarioMultiplo" class="control-label">Beneficiario multiplo</label>
			<div class="controls">
				<s:checkbox id="beneficiarioMultiplo" name="documento.flagBeneficiarioMultiplo" />
			</div>
		</div>
	</s:if>
	<s:else>
		<h4 class="step-pane">Soggetto
			<span id="descrizioneCompletaSoggetto">
				<s:if test='%{soggetto != null && (soggetto.codice ne null && soggetto.codice != "") && (soggetto.descrizione ne null && soggetto.descrizione != "") && (soggetto.codiceFiscale ne null && soggetto.codiceFiscale != "")}'>
					<s:property value="%{soggetto.codice + ' - ' + soggetto.descrizione + ' - ' + soggetto.codiceFiscale}" />
				</s:if>
			</span>
		</h4>
		<s:hidden id="HIDDEN_soggettoDenominazione" name="soggetto.denominazione" />
		<s:hidden id="HIDDEN_soggettoCodiceFiscale" name="soggetto.codiceFiscale" />
		<div class="control-group">
			<label class="control-label" for="codiceSoggetto">Codice *</label>
			<div class="controls">
				<s:textfield id="codiceSoggetto" cssClass="lbTextSmall span2" name="soggetto.codiceSoggetto" maxlength="20" placeholder="codice" disabled="true"/>
			</div>
		</div>
		<s:if test="fatturaFELPresente">
			<div class="control-group">
				<label class="control-label" for="identificativoFiscaleFEL">Identificatovo fiscale FEL</label>
				<div class="controls">
					<s:textfield id="identificativoFiscaleFEL" cssClass="lbTextSmall span2" readonly="true" value="%{identificativoFiscaleFEL}"/>
					<span class="alRight">
						<label for="codiceFiscaleFEL" class="radio inline">Codice fiscale FEL</label>
					</span>
					<s:textfield id="codiceFiscaleFEL" cssClass="lbTextSmall span2" maxlength="20" readonly="true" value="%{codiceFiscaleFEL}" />
				</div>
			</div>
		</s:if>
		<div class="control-group">
			<label class="control-label">Struttura Amministrativa</label>
			<div class="controls">
				<div class="accordion span8 struttAmm">
					<div class="accordion-group">
						<div class="accordion-heading">
							<a class="accordion-toggle" id="accordionPadreStrutturaAmministrativa_Doc" href="#struttAmm_Doc">
								<span id="SPAN_StrutturaAmministrativoContabile_Doc">Seleziona la Struttura amministrativa</span>
							</a>
						</div>
						<div id="struttAmm_Doc" class="accordion-body collapse">
							<div class="accordion-inner">
								<ul id="treeStruttAmm_Doc" class="ztree treeStruttAmm"></ul>
							</div>
						</div>
					</div>
				</div>

				<s:hidden id="HIDDEN_StrutturaAmministrativoContabile_DocUid" name="documento.strutturaAmministrativoContabile.uid" />
				<s:hidden id="HIDDEN_StrutturaAmministrativoContabile_DocCodice" name="documento.strutturaAmministrativoContabile.codice" />
				<s:hidden id="HIDDEN_StrutturaAmministrativoContabile_DocDescrizione" name="documento.strutturaAmministrativoContabile.descrizione" />
			</div>
		</div>
		<div class="control-group">
			<label for="beneficiarioMultiplo" class="control-label">Beneficiario multiplo</label>
			<div class="controls">
				<s:checkbox id="beneficiarioMultiplo" name="documento.flagBeneficiarioMultiplo" />
			</div>
		</div>
	</s:else>
	
	<%-- fine parte modificata --%>
	<h4 class="step-pane">Altri dati</h4>
	<div class="control-group">
		<label class="control-label" for="terminePagamentoDocumento">Termine di pagamento</label>
		<div class="controls">
			<s:textfield id="terminePagamentoDocumento" name="documento.terminePagamento" cssClass="lbTextSmall span2 numeroNaturale" placeholder="termine pagamento" />
			<span class="alRight">
				<label class="radio inline" for="dataScadenzaDocumento">Data scadenza</label>
			</span>
			<s:textfield id="dataScadenzaDocumento" name="documento.dataScadenza" cssClass="lbTextSmall span2 datepicker" placeholder="data scadenza" />
		</div>
	</div>

	<div class="control-group">
		<label class="control-label">Dati repertorio/protocollo</label>
		<div class="controls">
			<span class="alRight">
				<label class="radio inline" for="registroRepertorioDocumento">Registro</label>
			</span>
			<s:textfield id="registroRepertorioDocumento" name="documento.registroRepertorio" cssClass="lbTextSmall span2" placeholder="registro" />
			<span class="alRight">
				<label class="radio inline" for="annoRepertorioDocumento">Anno</label>
			</span>
			<s:textfield id="annoRepertorioDocumento" name="documento.annoRepertorio" cssClass="lbTextSmall span2" maxlength="4" placeholder="anno"/>
			<span class="alRight">
				<label class="radio inline" for="numeroRepertorioDocumento">Numero</label>
			</span>
			<s:textfield id="numeroRepertorioDocumento" name="documento.numeroRepertorio" cssClass="lbTextSmall span2" placeholder="numero" />
			<span class="alRight">
				<label class="radio inline" for="dataRepertorioDocumento">Data</label>
			</span>
			<s:textfield id="dataRepertorioDocumento" name="documento.dataRepertorio" cssClass="lbTextSmall span2 datepicker" placeholder="data" />
		</div>
	</div>


	<div class="control-group">
		<label class="control-label" for="codiceFiscalePignoratoDocumento">Soggetto pignorato</label>
		<div class="controls">
			<s:textfield id="codiceFiscalePignoratoDocumento" name="documento.codiceFiscalePignorato" cssClass="lbTextSmall span4" placeholder="codice fiscale" maxlength="16" />
		</div>
	</div>
	
	<%-- SIAC 6677 --%>
	<%-- SIAC-6840 riabilitazione campo --%>
	<div class="control-group"> 
		<label class="control-label" for="codAvvisoPagoPA">Codice Avviso PagoPA</label> 
		<div class="controls"> 
			<s:textfield id="codAvvisoPagoPA" name="documento.codAvvisoPagoPA" cssClass="lbTextSmall span2 numeroNaturale" placeholder="Codice Avviso PagoPA" />
		</div> 
	</div> 
	<%-- SIAC-6840 riabilitazione campo --%>

	<div class="step-pane active" id="datiPagamentoPadre">
		<div class="accordion" >
			<div class="accordion-group">
				<div class="accordion-heading">
					<a class="accordion-toggle" data-toggle="collapse" data-parent="#datiPagamentoPadre" href="#datiPagamentoTab">
						Dati pagamento<span class="icon">&nbsp;</span>
					</a>
				</div>
				<div id="datiPagamentoTab" class="accordion-body collapse in">
					<div class="accordion-inner">
						<fieldset class="form-horizontal">
							<div class="control-group">
								<label for="codiceBolloDocumento" class="control-label">Imposta di bollo *</label>
								<div class="controls input-append">
									<s:select list="listaCodiceBollo" name="documento.codiceBollo.uid" headerKey="0" headerValue=""
											listKey="uid" listValue="%{codice + '-' + descrizione}" cssClass="span9" required="required" />
								</div>
							</div>
							<div class="control-group">
								<label for="tipoImpresaDocumento" class="control-label">Tipo impresa</label>
								<div class="controls ">
									<s:select list="listaTipoImpresa" name="documento.tipoImpresa.uid" headerKey="0" headerValue=""
											listKey="uid" listValue="%{codice + '-' + descrizione}" cssClass="span9" />
								</div>
							</div>
							
							<div class="control-group">
								<label for="noteDocumento" class="control-label">Note</label>
								<div class="controls">
									<s:textarea id="noteDocumento" name="documento.note" rows="3" cols="15" cssClass="span10"></s:textarea>
								</div>
							</div>
							<div class="control-group">
								<label for="codicePCCDocumento" class="control-label">Codice PCC<s:if test="codicePccObbligatorio"> *</s:if></label>
								<div class="controls input-append">
									<s:select id="codicePCCDocumento" list="listaCodicePCC" name="documento.codicePCC.uid" headerKey="0" headerValue=""
											listKey="uid" listValue="%{codice + ' - ' + descrizione}" cssClass="span10" data-pcc-obbligatorio="%{codicePccObbligatorio}" required="%{codicePccObbligatorio}" data-disabilita-pcc="%{inibisciModificaDatiImportatiFEL}" />
								</div>
							</div>
							<div class="control-group">
								<label for="codiceUfficioDestinatarioDocumento" class="control-label">Codice Ufficio Destinatario FEL</label>
								<div class="controls input-append">
									<s:select id="codiceUfficioDestinatarioDocumento" list="listaCodiceUfficioDestinatarioPCC" name="documento.codiceUfficioDestinatario.uid" headerKey="0" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" cssClass="span10" data-disabilita-pcc="%{inibisciModificaDatiImportatiFEL}" disabled="%{fatturaFELPresente && inibisciModificaDatiImportatiFEL}"/>
									<s:if test="%{fatturaFELPresente && inibisciModificaDatiImportatiFEL}">
										<s:hidden id="" name="documento.codiceUfficioDestinatario.uid"/>
									</s:if>
								</div>
							</div>
						</fieldset>
					</div>
				</div>
			</div>
		</div>
	</div>
	<s:if test="datiFatturaPagataIncassataEditabili">
		<div class="step-pane active" id="datiFatturaPagataIncassataPadre">
			<div class="accordion" >
				<div class="accordion-group">
					<div class="accordion-heading">
						<a class="accordion-toggle" data-toggle="collapse" data-parent="#datiFatturaPagataIncassataPadre" href="#datiFatturaPagataIncassataTab">
							Dati fattura pagata<span class="icon">&nbsp;</span>
						</a>
					</div>
					<div id="datiFatturaPagataIncassataTab" class="accordion-body collapse">
						<div class="accordion-inner">
							<fieldset class="form-horizontal">
								<div class="control-group">
									<label for="flagPagataIncassataDatiFatturaPagataIncassata" class="control-label">Documento pagato</label>
									<div class="controls">
										<s:checkbox id="flagPagataIncassataDatiFatturaPagataIncassata" name="documento.datiFatturaPagataIncassata.flagPagataIncassata" />
									</div>
								</div>
								<div class="control-group">
									<label for="notePagamentoIncassoDatiFatturaPagataIncassata" class="control-label">Note</label>
									<div class="controls">
										<s:textfield id="notePagamentoIncassoDatiFatturaPagataIncassata" name="documento.datiFatturaPagataIncassata.notePagamentoIncasso" cssClass="lbTextSmall span9" />
									</div>
								</div>
								<div class="control-group">
									<label for="dataOperazioneDatiFatturaPagataIncassata" class="control-label">Data pagamento</label>
									<div class="controls">
										<s:textfield id="dataOperazioneDatiFatturaPagataIncassata" name="documento.datiFatturaPagataIncassata.dataOperazione" cssClass="lbTextSmall span2 datepicker" />
									</div>
								</div>
							</fieldset>
						</div>
					</div>
				</div>
			</div>
		</div>
	</s:if><s:else>
		<s:hidden name="documento.datiFatturaPagataIncassata.flagPagataIncassata" />
		<s:hidden name="documento.datiFatturaPagataIncassata.notePagamentoIncasso" />
		<s:hidden name="documento.datiFatturaPagataIncassata.dataOperazione" />
	</s:else>
	
	
	<div class="Border_line"></div>
	<p class="margin-medium">
		<s:include value="/jsp/include/indietro.jsp" />
		<s:reset cssClass="btn" value="annulla" />
		<a id="pulsanteAttivaRegistrazioniContabili" class="btn btn-secondary<s:if test="%{!attivaRegistrazioniContabiliVisible}"> hide</s:if>">
			attiva registrazioni contabili&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteAttivaRegistrazioniContabili"></i>
		</a>
		<s:submit cssClass="btn btn-primary pull-right" value="salva" />
	</p>
</s:form>
<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
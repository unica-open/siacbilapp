<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<s:form id="formAggiornamentoDatiDocumento" cssClass="form-horizontal" novalidate="novalidate" action="aggiornamentoDocumentoEntrata_aggiornamentoAnagrafica">
	<h4 class="step-pane">Dati principali</h4>
	<fieldset id="fieldset_AggiornaDocumentoEntrata">
		<div class="control-group">
			<label for="dataEmissioneDocumento" class="control-label">Data *</label>
			<div class="controls">
				<s:textfield id="dataEmissioneDocumento" name="documento.dataEmissione" cssClass="lbTextSmall span2 datepicker" size="10" required="required" />
				<%-- SIAC 6677 --%>
				<span class="alRight">
					<label class="radio inline" for="dataOperazioneDocumento">Data Operazione</label>
				</span>
				<s:textfield id="dataOperazioneDocumento" name="documento.dataOperazione" cssClass="lbTextSmall span2 datepicker" size="10" />
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
					<label for="documentiCollegatiDocumento" class="radio inline">Documenti Collegati</label>
				</span>
				<s:textfield id="documentiCollegatiDocumento" name="totaleDocumentiCollegati" cssClass="lbTextSmall span2" disabled="true" />
			</div>
		</div>

		<div class="control-group">
			<label for="descrizioneDocumento" class="control-label">Descrizione *</label>
			<div class="controls">
				<s:textarea id="descrizioneDocumento" name="documento.descrizione" cols="15" rows="2" cssClass="span10" required="required"></s:textarea>
			</div>
		</div>
		
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
				<label class="control-label" for="codiceSoggetto">Codice </label>
				<div class="controls">
					<s:textfield id="codiceSoggetto" cssClass="lbTextSmall span2" name="soggetto.codiceSoggetto" maxlength="20" placeholder="codice" required="required" />
					<span class="radio guidata">
						<a href="#" class="btn btn-primary" id="pulsanteAperturaCompilazioneGuidataSoggetto">compilazione guidata</a>
					</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="debitoreMultiplo">Debitore multiplo</label>
				<div class="controls">
				<s:checkbox id="debitoreMultiplo" name="documento.flagDebitoreMultiplo" />
				</div>
			</div>
			<%-- SIAC-7567 metto il campo solo se é una PA --%>
			<s:if test="checkCanale != null && checkCanale == true">
				<s:hidden id="HIDDEN_checkCanale" name="checkCanale" />
			</s:if>
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
			<div class="control-group">
				<label for="beneficiarioMultiplo" class="control-label">Beneficiario multiplo</label>
				<div class="controls">
					<s:checkbox id="beneficiarioMultiplo" name="documento.flagBeneficiarioMultiplo" />
				</div>
			</div>
			<%-- SIAC-7567 metto il campo solo se é una PA --%>
			<s:if test="checkCanale != null && checkCanale == true">
				<s:hidden id="HIDDEN_checkCanale" name="checkCanale" />
			</s:if>
		</s:else>
		
		<%-- fine parte modificata --%>
		<h4 class="step-pane">Altri dati</h4>
		<div class="control-group">
			<label class="control-label" for="termineIncassoDocumento">Termine di pagamento</label>
			<div class="controls">
				<s:textfield id="termineIncassoDocumento" name="documento.terminePagamento" cssClass="lbTextSmall span2 soloNumeri" placeholder="termine pagamento" />
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
		
		<%-- SIAC 6677 --%>
		<div class="control-group">
			<label class="control-label" for="codAvvisoPagoPA">Codice Avviso Pago PA</label>
			<div class="controls">
				<s:textfield id="codAvvisoPagoPA" name="documento.codAvvisoPagoPA" cssClass="lbTextSmall span2 numeroNaturale" placeholder="Codice Avviso Pago PA" />
				<span class="alRight">
					<label class="radio inline" for="iuv">IUV</label>
				</span>
				<s:textfield id="iuv" name="documento.iuv" readonly="true" cssClass="lbTextSmall span2" placeholder="IUV" />
			</div>
		</div>
		
		<%-- SIAC-7567 --%>
		<div class="control-group">
			<label class="control-label" for="cig">CIG</label>
			<div class="controls">
				<s:textfield id="cig" name="documento.cig" cssClass="lbTextSmall span3 cig" maxlength="10" placeholder="CIG" />
				<span class="alRight">
					<label class="radio inline" for="cup">CUP</label>
				</span>
				<s:textfield id="cup" name="documento.cup" cssClass="lbTextSmall span3 cup" maxlength="15" placeholder="CUP" />
			</div>
		</div>
		<s:hidden id="HIDDEN_proseguireConElaborazioneCheckPA" name="proseguireConElaborazioneCheckPA" value="false"/>

		<div class="step-pane active" id="datiIncassoPadre">
			<div class="accordion" >
				<div class="accordion-group">
					<div class="accordion-heading">
						<a class="accordion-toggle" data-toggle="collapse" data-parent="#datiIncassoPadre" href="#datiIncassoTab">
							Dati incasso<span class="icon">&nbsp;</span>
						</a>
					</div>
					<div id="datiIncassoTab" class="accordion-body collapse in">
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
									<label for="noteDocumento" class="control-label">Note</label>
									<div class="controls">
										<s:textarea id="noteDocumento" name="documento.note" rows="3" cols="15" cssClass="span10"></s:textarea>
									</div>
								</div>
							</fieldset>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="Border_line"></div>
		<p class="margin-medium">
			<s:include value="/jsp/include/indietro.jsp" />
			<s:reset cssClass="btn" value="annulla" />
			
			<a id="pulsanteAttivaRegistrazioniContabili" class="btn btn-secondary<s:if test="%{!attivaRegistrazioniContabiliVisible}"> hide</s:if>">
				attiva registrazioni contabili&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteAttivaRegistrazioniContabili"></i>
			</a>
			<s:submit id="salvaAggiornamento" cssClass="btn btn-primary pull-right" value="salva" />
		</p>
	</fieldset>
</s:form>
<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
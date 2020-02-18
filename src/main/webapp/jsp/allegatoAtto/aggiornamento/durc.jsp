<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<s:form id="formAggiornaAllegatoAttoElenchi" cssClass="form-horizontal" novalidate="novalidate" action="#" method="post">

	<fieldset class="form-horizontal">
		<br>
		<div id="accordionDurc" class="accordion">
			<div class="accordion-group">
				<div class="accordion-heading">
					<a href="#divDurc" data-parent="#accordionDurc" data-toggle="collapse" class="accordion-toggle collapsed">
						Riepilogo DURC     <span class="icon">&nbsp;</span>
					</a>
				</div>
				<div class="accordion-body collapse" id="divDurc">
					<div class="accordion-inner">
						<fieldset class="form-horizontal">
							<table class="table table-hover tab_left" id="tabellaElencoSoggetti">
								<thead>
									<tr>
										<th>Soggetto</th>
										<th>Denominazione</th>
										<th>Codice fiscale</th>
										<th>Partita IVA</th>
										<th>Data fine validit&agrave; DURC</th>
										<th>Login modifica DURC</th>
										<th>Fonte</th>
										<th></th>
										<!-- test -->
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</fieldset>
					</div>
				</div>
			</div>
		</div>
		
		
		<div id="dettaglioSoggetto" class="hide accordion-group">

				<table class="table table-hover tab_left" id="headerSoggetto">
					<thead>
						<tr>
							<th id="denominazioneSoggetto"></th>
						</tr>
					</thead>
				</table>
			
	           <div class="control-group">
	                <label class="control-label" for="codfisc">Data fine validit&agrave; DURC</label>
	                <div class="controls">
	
	                   <s:textfield id="dataFineValiditaDurc" name="soggetto.dataFineValiditaDurc" 
	                              cssClass="span1 datepicker" maxlength="16" /> 
				  </div>
	          </div>
			
			<s:hidden id="tipoFonteDurc" name="soggetto.tipoFonteDurc" value="M"/>

			<div class="control-group " id="fonteDurcClassifIdDiv">
	
				<label class="control-label">Fonte DURC</label>
				<div class="controls">
					<div class="accordion span8 struttAmm" id="accordionStrutturaAmministrativaContabile">
						<div class="accordion-group">
							<div class="accordion-heading">
								<a class="accordion-toggle" id="accordionPadreStrutturaAmministrativa" href="#collapseStrutturaAmministrativaContabile"
										data-parent="#accordionStrutturaAmministrativaContabile">
									<span id="SPAN_StrutturaAmministrativoContabile">Seleziona la Struttura amministrativa</span>
								</a>
							</div>
							<div id="collapseStrutturaAmministrativaContabile" class="accordion-body collapse">
								<div class="accordion-inner">
									<ul id="treeStruttAmm" class="ztree treeStruttAmm"></ul>
								</div>
							</div>
						</div>
					</div>

					<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUid" name="soggetto.fonteDurcClassifId" />
					<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodice" name="strutturaAmministrativoContabile.codice" />
					<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizione" name="strutturaAmministrativoContabile.descrizione" />
				</div>
	
			</div>
  
           <div class="control-group" id="noteDurcDiv">
                <label class="control-label" for="codfisc">Note DURC</label>
                <div class="controls">
                   <s:textfield id="noteDurc" name="soggetto.noteDurc" 
                              cssClass="span7 "  /> 
			  
			  </div>
          </div>
			<div >
						<s:hidden id="idSoggetto" name="soggetto.uid" value=""/>
			
				<button id="salvaDurc" class="btn btn-primary pull-right">salva</button><br/><br/><br/>
			</div>

		</div>
	</fieldset>

	<p class="margin-medium">
		<s:include value="/jsp/include/indietro.jsp" />
	</p>
	<s:include value="/jsp/allegatoAtto/associaElencoDocumentiAllegato_modale.jsp" />
	<s:include value="/jsp/include/modaleConfermaEliminazione.jsp" />
</s:form>
<div id="containerModaleAggiornamentoQuotaElenco"></div>
<div id="containerSlideSpezzaQuotaElenco" class="hide"></div>
<s:include value="/jsp/provvisorioCassa/modaleRicercaProvvisorioCassa.jsp" />


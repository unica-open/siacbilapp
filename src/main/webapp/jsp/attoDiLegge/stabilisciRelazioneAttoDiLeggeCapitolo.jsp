<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<%-- Messaggio di ERRORI --%>
<div id="ERRORI_CERCA_REL" class="alert alert-error hide">
	<button type="button" class="close" data-hide="alert">&times;</button>
	<strong>Attenzione!!</strong><br>
	<ul>
	</ul>
</div>

<%-- Messaggio di INFORMAZIONI --%>
<div id="INFORMAZIONI_CERCA_REL" class="alert alert-success hide">
		<button type="button" class="close" data-hide="alert">&times;</button>
		<strong>Attenzione!!</strong><br>
		<ul>
		</ul>
</div>

<s:include value="/jsp/attoDiLegge/elencoRelazioniAttoDiLeggeCapitolo.jsp" />
<s:include value="/jsp/attoDiLegge/aggiornaRelazioneAttoDiLeggeCapitolo.jsp" />
<p>
	<a class="btn" title="inserisci nuova relazione" data-toggle="collapse" id="pulsantePreparaInserimentoRelazione" data-target="#divInsNuovaRelazione">inserisci nuova relazione </a>
</p>

<div id="divInsNuovaRelazione" class="accordion-body collapse">
	<div class="accordion-inner">
	
		<s:form action="formRicercaAttoDiLegge" method="post" novalidate="novalidate">
			<s:include value="/jsp/attoDiLegge/ricercaAttoDiLegge.jsp" />
			<s:include value="/jsp/attoDiLegge/risultatiRicercaAttoDiLeggeSenzaOperazioni.jsp" />
		</s:form>
		<%-- Pulsanti "stabilisci relazione" , "inserisci nuovi atti di legge", "annulla atto di legge" --%>
		<p>
			<a data-toggle="collapse" data-target="#relazioneAttoDiLeggeCapitolo" class="btn" id="pulsantePreparaAssociaRelazione">stabilisci relazione</a>
			<span class="nascosto"> | </span>
			<a href="#insAtto" id="pulsantePreparaInserisciNuovoAttoDiLegge" data-toggle="modal" class="btn">
			inserisci nuovi atti di legge
			</a>
			<span class="nascosto"> | </span>
			<a href="#annullaAtto" id="pulsantePreparaAnnullaAttoDiLegge" data-toggle="modal" class="btn">annulla atto di legge</a> 
		</p>
	
		<%-- Modale per l'inserimento di un nuovo atto di legge --%>
		<div id="insAtto" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgEliminaLabel" aria-hidden="true">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 id="myModalLabel">Inserisci nuovi atti di legge</h4>
				<div id="ERRORI_INSERISCI_ADL" class="alert alert-error hide">
					<button type="button" class="close" data-hide="alert">&times;</button>
					<strong>Attenzione!!</strong><br>
					<ul>
					</ul>
				</div>
				<div id="INFORMAZIONI_INSERISCI_ADL" class="alert alert-success hide">
					<button type="button" class="close" data-hide="alert">&times;</button>
					<strong>Attenzione!!</strong><br>
					<ul>
					</ul>
				</div>
			</div>
			<div id="divInserisciAttoDiLegge" class="modal-body">
				<s:form novalidate="novalidate" action="formInserisciAttoDiLegge" method="post">
				<s:include value="/jsp/attoDiLegge/inserisciAttoDiLegge.jsp" />
				</s:form>
			</div>
			<div class="modal-footer">
				<button id="btnModalInsAttoChiudi" class="btn hide" data-dismiss="modal" aria-hidden="true">chiudi</button>
				<button id="btnModalInsAttoAnnulla" class="btn" data-dismiss="modal" aria-hidden="true">annulla inserimento</button>
				<a id="btnModalInsAttoSalva" class="btn btn-primary" href="#">
					salva
				</a>
			
			</div>
		</div>
		<!--/ Modale Inserimento -->
	
		<%-- Modale per l'annullamento di un atto di legge --%>
		<div id="annullaAtto" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
		
			<div class="modal-body">
				<div id="divAvvisoAnnullamento" class="alert alert-error">
					<button type="button" class="close" data-hide="alert">&times;</button>
					<p><strong>Attenzione!</strong></p>
					<p>Stai per annullare l'elemento selezionato, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
				</div>
				<div id="ERRORI_ANNULLA_ATTO"  class="alert alert-error hide">
					<button type="button" class="close" data-hide="alert">&times;</button>
					<strong>Attenzione!!</strong><br>
					<ul>
					</ul>
				</div>	
				
				<%-- Messaggio di INFORMAZIONI --%>
				<div id="INFORMAZIONI_ANNULLA_ATTO" class="alert alert-success hide">
						<button type="button" class="close" data-hide="alert">&times;</button>
						<strong>Attenzione!!</strong><br>
						<ul>
						</ul>
				</div>
			</div>
			
			<div class="modal-footer">
				<button id="btnAnnullaAttoChiudi" class="btn" data-dismiss="modal" aria-hidden="true">chiudi</button>
				<button id="btnAnnullaAttoIndietro" class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
				<button id="btnAnnullaAttoProcedi" class="btn btn-primary">si, prosegui</button>
			</div>
		</div>  
		<!--/ Modale Annulla -->
	  
		<%-- Modale per la conferma del salvataggio relazione avvenuto --%>
		<div id="msgSalvaRelazione" class="alert alert-info hide">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong>
			<br />
			I dati sono stati inseriti correttamente
		</div>
	
		<div class="accordion-body collapse" id="relazioneAttoDiLeggeCapitolo" >
			<div id="ERRORI_INS_RELAZIONE" class="alert alert-error hide">
				<button type="button" class="close" data-hide="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
				<ul>
				</ul>
			</div>	
				
			<%-- Messaggio di INFORMAZIONI --%>
			<div id="INFORMAZIONI_INS_RELAZIONE" class="alert alert-success hide">
				<button type="button" class="close" data-hide="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
				<ul>
				</ul>
			</div>			
			<s:form action="inserisciRelazioneAttoDiLeggeCapitolo" novalidate="novalidate" method="post">
				<fieldset class="form-horizontal">
					<div class="control-group">
						<label class="control-label" for="fileb">Gerarchia</label>
						<div class="controls">
							<s:textfield id="relGerarchia" name="gerarchia" id="fileb" cssClass="span4" maxlength="200"/> <%-- Gestione della lunghezza del campo --%>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="file_11">Descrizione</label>
						<div class="controls">
							<s:textarea name="descrizione" id="file_111" cssClass="span4" maxlength="500" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="file_12">Data inizio finanziamento</label>
						<div class="controls">
							<s:textfield id="dataInizioFinanziamento" name="dataInizioFinanziamento" id="file_12" cssClass="span2 datepicker" size="16" /> <%-- Gestione della data --%>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="file_13">Data fine finanziamento</label>
						<div class="controls">
							<s:textfield id="dataFineFinanziamento" name="dataFineFinanziamento" id="file_13" cssClass="span2 datepicker" size="16" /> <%-- Gestione della data --%>
						</div>
					</div>
					<s:hidden name="uidCapitolo" id="uidCapitolo" value="" data-maintain="" />
					<s:hidden name="uidAttoDiLegge" id="uidAttoDiLegge" value="" data-maintain="" />
				</fieldset>
				<p>
					<button type="button" class="btn btn-link reset">annulla inserimento</button>
					&nbsp;
					<a id="btnModalInsRelazioneSalva" class="btn btn-primary" href="#">salva</a>
				</p>
			</s:form>
		</div>
	</div>
</div>
<s:include value="/jsp/include/indietro.jsp" />
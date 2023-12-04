<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<s:include value="/jsp/include/head.jsp" />
</head>

<body>
	<s:include value="/jsp/include/header.jsp" />
	
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="contentPage">
					<s:include value="/jsp/include/messaggi.jsp"/>
               <s:form id="formMovimentiGestioneAssociatiMutuo" cssClass="form-horizontal" >
                   <s:hidden name="mutuo.uid" />
                   <span class="hide" data-anno-bilancio='<s:property value="getAnnoBilancio()"/>'></span>
                 		
                     <h3>Movimenti contabili (pagamento opere, progetti e iniziative, rimborso prestito)</h3>

                  <div class="step-content">
                     <div class="step-pane active" id="step1">

                       <fieldset class="form-horizontal">
                           <h4>Dati mutuo</h4>
                           <div class="control-group">
                              <label class="control-label">Numero Mutuo</label>
                              <div class="controls">
                                 <s:textfield type="text" disabled="true" class="lbTextSmall span1" name="mutuo.numero" id="numeroMutuo" />
                                 
                                 <span class="al"> <label class="radio inline" for="tipoTasso">Tipo Tasso</label>
                                 </span> <s:textfield type="text" disabled="true" class="lbTextSmall span1" name="mutuo.tipoTasso" id="tipoTasso"/>

                                 <span class="al"> <label class="radio inline" for="sommaMutuataIniziale">Importo</label></span>
                                 <s:textfield disabled="true" id="sommaMutuataIniziale" name="mutuo.sommaMutuataIniziale" cssClass="span2 decimale soloNumeri forzaVirgolaDecimale"  />
                              </div>
                           </div>

                           <div class="control-group">
                              <label class="control-label">Istituto di Credito</label>
                              <div class="controls">
                                 <s:textfield type="text" disabled="true" class="lbTextSmall span1" name="mutuo.soggetto.codiceSoggetto" id="codiceSoggetto" />
                                 <s:textfield type="text" disabled="true" class="lbTextSmall span6" name="mutuo.soggetto.denominazione" />
                              </div>
                           </div>

                           <h4>Impegni</h4>
                           <s:if test="not mutuo.elencoImpegniAssociati.empty">
                              <s:include value="/jsp/mutuo/include/elencoImpegniAssociatiMutuo.jsp" />
                           </s:if><s:else>
                              Non sono presenti impegni associati.
                           </s:else>
                           
                           <h4>Accertamenti</h4>
                           <s:if test="not mutuo.elencoAccertamentiAssociati.empty">
                              <s:include value="/jsp/mutuo/include/elencoAccertamentiAssociatiMutuo.jsp" />
                           </s:if><s:else>
                              Non sono presenti accertamenti associati.
                           </s:else>

                      <p/>  
                  <br/>
                         </fieldset>
                     </div>
                  </div>
                  <br/>
						<p>
							
                     		 <s:include value="/jsp/include/indietro.jsp" />
                     		 
                             <s:submit value="esporta movimenti associati in excel" cssClass="btn scaricaElencoMovimentiAssociati disableOverlay" action="movimentiGestioneAssociatiMutuo_scaricaElencoExcel" disabled="mutuo.elencoImpegniAssociati.empty && mutuo.elencoAccertamentiAssociati.empty"/>
                             <s:submit id="eliminaAssociazione" value="elimina associazione movimenti selezionati" disabled="true" cssClass="btn" action="movimentiGestioneAssociatiMutuo_eliminaAssociazione" />
							 <s:submit value="cerca movimenti contabili" cssClass="btn" action="ricercaMovimentoGestioneMutuo" />
							 
						</p>
                  
                                    <s:include value="/jsp/mutuo/include/dettaglioMovimentoGestioneMutuo_modale.jsp" />
                                    <s:include value="/jsp/mutuo/include/dettaglioAccertamentoMutuo_modale.jsp" />
                  
					</s:form>
				</div>	
			</div>	
		</div>	 
	</div>	

	<s:include value="/jsp/include/footer.jsp" />

	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/mutuo/movimentiGestioneAssociatiMutuo.js"></script>
</body>
</html>